package pl.lodz.pl.it.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import pl.lodz.pl.it.auth.dto.AuthenticationRequest;
import pl.lodz.pl.it.auth.dto.AuthenticationResponse;
import pl.lodz.pl.it.auth.dto.RegisterRequest;
import pl.lodz.pl.it.entity.Account;
import pl.lodz.pl.it.entity.AccountConfirmation;
import pl.lodz.pl.it.entity.JWTWhitelistToken;
import pl.lodz.pl.it.entity.Role;
import pl.lodz.pl.it.mok.repository.AccountRepository;
import pl.lodz.pl.it.auth.repository.AccountConfirmationRepository;
import pl.lodz.pl.it.auth.repository.JWTWhitelistRepository;
import pl.lodz.pl.it.auth.repository.RoleRepository;
import pl.lodz.pl.it.security.JwtService;
import pl.lodz.pl.it.utils.RandomPasswordGenerator;
import pl.lodz.pl.it.utils.RunAs;
import pl.lodz.pl.it.utils.TokenGenerator;
import pl.lodz.pl.it.utils._enum.AccountRoleEnum;
import pl.lodz.pl.it.utils.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static pl.lodz.pl.it.utils.Utils.calculateExpirationDate;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final TokenVerifier tokenVerifier;
  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  private final RoleRepository roleRepository;
  private final MailService mailService;

  private final AccountConfirmationRepository accountConfirmationRepository;
  private final JWTWhitelistRepository jwtWhitelistRepository;

  @Transactional
  public AuthenticationResponse register(RegisterRequest request) {
    try {
      var account = Account.builder()
          .firstName(request.getFirstName())
          .lastName(request.getLastName())
          .email(request.getEmail())
          .password(passwordEncoder.encode(request.getPassword()))
          .enabled(false)
          .build();

      var savedAccount = accountRepository.saveAndFlush(account);
      var randString = TokenGenerator.generateToken();

      var expirationHours = 24;
      var expirationDate = calculateExpirationDate(expirationHours);
      var newAccountConfirmation = new AccountConfirmation(randString, account, expirationDate);

      accountConfirmationRepository.saveAndFlush(newAccountConfirmation);

      try {
        mailService.sendEmailToVerifyAccount(savedAccount, randString);
      } catch (Exception e) {

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Error sending email verification");
      }

      return AuthenticationResponse.builder().build();


    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Ten email jest już zajęty");
    }
  }


  @Transactional
  public String authenticate(AuthenticationRequest request) {

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              request.getEmail(),
              request.getPassword()
          )
      );
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Niepoprawnie wprowadzone dane");
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
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Confirmation token not found"));

    if (accountConfirmation.getExpirationDate().isBefore(LocalDateTime.now())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
          "Account confirmation token expired");
    }

    var accountId = accountConfirmation.getAccount().getId();
    var account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

    account.setEnabled(true);
    account.setRole(roleRepository.findByName(AccountRoleEnum.ROLE_USER)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found")));

    accountRepository.saveAndFlush(account);

    accountConfirmationRepository.delete(accountConfirmation);

    RunAs.runAsSystem(() -> mailService.sendEmailToInformAboutVerification(account));
  }

  @Transactional
  public void logout(UUID accountId) {
    jwtWhitelistRepository.deleteAllByAccount_Id(accountId);
  }


  @Transactional
  public String refreshJWT(String token) {
    JWTWhitelistToken jwtWhitelistToken = jwtWhitelistRepository.findByToken(token.substring(7))
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not found"));

    Account account = jwtWhitelistToken.getAccount();
    jwtWhitelistRepository.delete(jwtWhitelistToken);
    jwtWhitelistRepository.flush();

    return jwtService.generateToken(new HashMap<>(), account);
  }


  @Transactional
  public AuthenticationResponse authenticateWithGoogle(String idToken) {
    GoogleIdToken token = this.tokenVerifier.verifyGoogle(idToken);
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
  }


  @Transactional
  public AuthenticationResponse authenticateWithAmazon(String accessToken) {
    accessToken = accessToken.trim().replace("\"", "");
    Map<String, Object> userProfile = tokenVerifier.verifyAmazon(accessToken);

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
        String lastName = nameParts.length > 1 ? nameParts[1]
            : ""; // Drugie słowo jako nazwisko (lub puste, jeśli brak)

        account = Account.builder()
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .enabled(true)
            .password(password)
            .role(role)
            .build();
        accountRepository.save(account);
      } else {
        throw new IllegalArgumentException("Email and name must not be null or empty");
      }
    }

    String jwtToken = jwtService.generateToken(new HashMap<>(), account);

    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();


  }


}
