package pl.lodz.pl.it.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

public interface TokenVerifier {
  GoogleIdToken verifyGoogle(String idToken);

  Map<String, Object> verifyAmazon(String accessToken);
}
