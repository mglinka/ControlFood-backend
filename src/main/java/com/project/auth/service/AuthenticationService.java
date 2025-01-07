package com.project.auth.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.project.auth.dto.AuthenticationRequest;
import com.project.auth.dto.AuthenticationResponse;
import com.project.auth.dto.RegisterRequest;
import com.project.entity.Account;
import com.project.entity.AccountConfirmation;
import com.project.entity.JWTWhitelistToken;
import com.project.entity.Role;
import com.project.mok.repository.AccountRepository;
import com.project.auth.repository.AccountConfirmationRepository;
import com.project.auth.repository.JWTWhitelistRepository;
import com.project.auth.repository.RoleRepository;
import com.project.security.JwtService;
import com.project.utils.RandomPasswordGenerator;
import com.project.utils.RunAs;
import com.project.utils.TokenGenerator;
import com.project.utils._enum.AccountRoleEnum;
import com.project.utils.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.project.utils.Utils.calculateExpirationDate;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;
    private final MailService mailService;

    private final AccountConfirmationRepository accountConfirmationRepository;
    private final JWTWhitelistRepository jwtWhitelistRepository;



    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.amazon.client-id}")
    private String amazonClientId;

    private static final String AMAZON_TOKEN_INFO_URL = "https://api.amazon.com/auth/o2/tokeninfo";
    private static final String AMAZON_PROFILE_URL = "https://api.amazon.com/user/profile";

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Transactional
    public AuthenticationResponse register (RegisterRequest request){


        var account = Account.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var savedAccount = accountRepository.saveAndFlush(account);
        var randString = TokenGenerator.generateToken();

        var expirationHours = 24;
        var expirationDate = calculateExpirationDate(expirationHours);
        var newAccountConfirmation = new AccountConfirmation(randString, account, expirationDate);

        accountConfirmationRepository.saveAndFlush(newAccountConfirmation);




        mailService.sendEmailToVerifyAccount(savedAccount,randString);

        return AuthenticationResponse.builder()
                .build();


    }


    @Transactional
    public String authenticate (AuthenticationRequest request){

        log.info("ELO");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid login credentials", e);
        }

        var account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Nie znaleziono konta"));

        if (!account.getEnabled()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Konto jest zablokowane");
        }
        accountRepository.saveAndFlush(account);
        return jwtService.generateToken(new HashMap<>(), account);

    }


    @PreAuthorize("permitAll()")
    @Transactional
    public void verifyAccount(String token) {
        var accountConfirmation = accountConfirmationRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Confirmation token not found"));

        if (accountConfirmation.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account confirmation token expired");
        }

        var accountId = accountConfirmation.getAccount().getId();
        var account = accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        account.setEnabled(true);
        account.setRole(roleRepository.findByName(AccountRoleEnum.ROLE_USER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found")));

        accountRepository.saveAndFlush(account);

        accountConfirmationRepository.delete(accountConfirmation);


        RunAs.runAsSystem(() -> mailService.sendEmailToInformAboutVerification(account));
    }

    public void logout(String token) {
        jwtWhitelistRepository.deleteByToken(token);
    }


    @Transactional
    public String refreshJWT(String token) {
        JWTWhitelistToken jwtWhitelistToken = jwtWhitelistRepository.findByToken(token.substring(7)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not found"));

        Account account = jwtWhitelistToken.getAccount();
        jwtWhitelistRepository.delete(jwtWhitelistToken);
        jwtWhitelistRepository.flush();

        return jwtService.generateToken(new HashMap<>(), account);
    }



    @Transactional
    public AuthenticationResponse authenticateWithGoogle(String idToken) {
        try {
            System.out.println("BartekFirst"+googleClientId);
            HttpTransport transport = new NetHttpTransport();


            JsonFactory jsonFactory = new GsonFactory();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(this.googleClientId))
                    .build();

            idToken = idToken.trim().replace("\"", "");
            System.out.println("Bartek1"+idToken);

            GoogleIdToken token = verifier.verify(idToken);
            if (token == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID Token");
            }

            GoogleIdToken.Payload payload = token.getPayload();
            String email = payload.getEmail();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");

            Account account = accountRepository.findByEmail(email).orElse(null);

            Role role = roleRepository.findByName(AccountRoleEnum.ROLE_USER)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));


            String password = passwordEncoder.encode(RandomPasswordGenerator.generateRandomPassword(16));
            if (account == null) {
                account = Account.builder()
                        .email(email)
                        .firstName(firstName)
                        .lastName(lastName)
                        .password(password)
                        .role(role)
                        .enabled(true)
                        .build();
                accountRepository.save(account);
            }



            String jwtToken = jwtService.generateToken(new HashMap<>(), account);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();

        } catch (GeneralSecurityException | IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error verifying token", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred during Google authentication", e);
        }
    }



    @Transactional
    public AuthenticationResponse authenticateWithAmazon(String accessToken) {
            try {
                accessToken = accessToken.trim().replace("\"", "");
                // Step 1: Validate the access token
                String tokenInfoUrl = AMAZON_TOKEN_INFO_URL + "?access_token=" +
                        java.net.URLEncoder.encode(accessToken, StandardCharsets.UTF_8);


                Map<String, Object> tokenInfo = sendGetRequest(tokenInfoUrl);

                if (!amazonClientId.equals(tokenInfo.get("aud"))) {
                    throw new IllegalArgumentException("Invalid token: The token does not belong to this client.");
                }

                // Step 2: Fetch user profile
                Map<String, Object> userProfile = sendAuthorizedGetRequest(AMAZON_PROFILE_URL, accessToken);

                System.out.println("Barr"+userProfile);
                String email = userProfile.get("email").toString();
                String name = userProfile.get("name").toString();
                Account account = accountRepository.findByEmail(email).orElse(null);
                Role role = roleRepository.findByName(AccountRoleEnum.ROLE_USER)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));


                String password = passwordEncoder.encode(RandomPasswordGenerator.generateRandomPassword(16));


                if (account == null) {
                    if (email != null && name != null && !name.trim().isEmpty()) {
                        String[] nameParts = name.split("\\s+"); // używamy regex \\s+ dla większej elastyczności
                        String firstName = nameParts.length > 0 ? nameParts[0] : ""; // Pierwsze imię
                        String lastName = nameParts.length > 1 ? nameParts[1] : ""; // Drugie słowo jako nazwisko (lub puste, jeśli brak)

                        account = Account.builder()
                                .email(email)
                                .firstName(firstName)
                                .lastName(lastName) // Ustawienie nazwiska
                                .enabled(true)
                                .password(password)
                                .role(role)
                                .build();
                        accountRepository.save(account);
                    } else {
                        // Obsługuje przypadek, gdy email lub name są null lub nieprawidłowe
                        throw new IllegalArgumentException("Email and name must not be null or empty");
                    }
                }


                String jwtToken = jwtService.generateToken(new HashMap<>(), account);

                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .build();

            } catch (Exception e) {
                throw new RuntimeException("Authentication with Amazon failed", e);
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

        private Map<String, Object> sendAuthorizedGetRequest(String url, String accessToken) throws Exception {
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
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
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
                return objectMapper.readValue(json, new TypeReference<>() {});
            } catch (Exception ex) {
                throw new RuntimeException("Failed to parse JSON response", ex);
            }
        }


}
