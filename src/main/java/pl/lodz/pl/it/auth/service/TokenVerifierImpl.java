package pl.lodz.pl.it.auth.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class TokenVerifierImpl implements TokenVerifier {

  private final GoogleIdTokenVerifier googleVerifier;
  private final ObjectMapper objectMapper = new ObjectMapper();
  @Value("${spring.security.oauth2.client.registration.amazon.client-id}")
  private String amazonClientId;

  private static final String AMAZON_TOKEN_INFO_URL = "https://api.amazon.com/auth/o2/tokeninfo";
  private static final String AMAZON_PROFILE_URL = "https://api.amazon.com/user/profile";

  public TokenVerifierImpl(
      HttpTransport transport,
      JsonFactory jsonFactory,
      @Value("${spring.security.oauth2.client.registration.google.client-id}")
      String googleClientId
  ) {
    this.googleVerifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(Collections.singletonList(googleClientId))
        .build();
  }

  @Override
  public GoogleIdToken verifyGoogle(String idToken) {
    try {
      return googleVerifier.verify(idToken.trim().replace("\"", ""));
    } catch (GeneralSecurityException | IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error verifying token", e);
    }
  }

  @Override
  public Map<String, Object> verifyAmazon(String accessToken) {
    String tokenInfoUrl = AMAZON_TOKEN_INFO_URL + "?access_token=" +
        java.net.URLEncoder.encode(accessToken, StandardCharsets.UTF_8);

    Map<String, Object> tokenInfo;
    try {
      tokenInfo = sendGetRequest(tokenInfoUrl);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authentication with Amazon failed");
    }

    if (!amazonClientId.equals(tokenInfo.get("aud"))) {
      throw new IllegalArgumentException(
          "Invalid token: The token does not belong to this client.");
    }

    try {
      return sendAuthorizedGetRequest(AMAZON_PROFILE_URL, accessToken);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authentication with Amazon failed");
    }

  }


  private Map<String, Object> sendGetRequest(String url) throws Exception {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("Accept", "application/json");

    int responseCode = connection.getResponseCode();
    if (responseCode != 200) {
      throw new RuntimeException("Failed: HTTP error code : " + responseCode);
    }

    String jsonResponse = readResponse(connection);
    return parseJson(jsonResponse);
  }

  private Map<String, Object> sendAuthorizedGetRequest(String url, String accessToken)
      throws Exception {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("Accept", "application/json");
    connection.setRequestProperty("Authorization", "Bearer " + accessToken);

    int responseCode = connection.getResponseCode();
    if (responseCode != 200) {
      throw new RuntimeException("Failed: HTTP error code : " + responseCode);
    }

    String jsonResponse = readResponse(connection);
    return parseJson(jsonResponse);
  }


  private String readResponse(HttpURLConnection connection) throws Exception {
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        response.append(line);
      }
      return response.toString();
    }
  }

  private Map<String, Object> parseJson(String json) {
    try {
      return objectMapper.readValue(json, new TypeReference<>() {
      });
    } catch (Exception ex) {
      throw new RuntimeException("Failed to parse JSON response", ex);
    }
  }


}
