package com.project.service;

import com.project.dto.auth.AuthenticationRequest;
import com.project.dto.auth.AuthenticationResponse;
import com.project.dto.auth.RegisterRequest;
import com.project.entity.Account;
import com.project.entity.AccountConfirmation;
import com.project.entity.JWTWhitelistToken;
import com.project.repository.AccountConfirmationRepository;
import com.project.mok.repository.AccountRepository;
import com.project.repository.JWTWhitelistRepository;
import com.project.repository.RoleRepository;
import com.project.security.JwtService;
import com.project.utils.RunAs;
import com.project.utils.TokenGenerator;
import com.project.utils._enum.AccountRoleEnum;
import com.project.utils.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;

import static com.project.utils.Utils.calculateExpirationDate;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;
    private final MailService mailService;

    private final AccountConfirmationRepository accountConfirmationRepository;
    private final JWTWhitelistRepository jwtWhitelistRepository;

    public AuthenticationResponse register (RegisterRequest request){


        var account = Account.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var savedAccount = repository.saveAndFlush(account);
        var randString = TokenGenerator.generateToken();

        var expirationHours = 24;
        var expirationDate = calculateExpirationDate(expirationHours);
        var newAccountConfirmation = new AccountConfirmation(randString, account, expirationDate);

        accountConfirmationRepository.saveAndFlush(newAccountConfirmation);




        mailService.sendEmailToVerifyAccount(savedAccount,randString);

        return AuthenticationResponse.builder()
                .build();


    }


    public String authenticate (AuthenticationRequest request){
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

        var account = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String hash = passwordEncoder.encode("P@ssword123");
        System.out.println(hash);
//        var jwtToken = jwtService.generateToken(account);

        repository.saveAndFlush(account);
        System.out.println(account.getEmail());
        System.out.println(account.getAuthorities());
        return jwtService.generateToken(new HashMap<>(), account);



//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();

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
        var account = repository.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        account.setEnabled(true);
        account.setRole(roleRepository.findByName(AccountRoleEnum.ROLE_USER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found")));

        repository.saveAndFlush(account);

        accountConfirmationRepository.delete(accountConfirmation);


        RunAs.runAsSystem(() -> mailService.sendEmailToInformAboutVerification(account));
    }

    public void logout(String token) {
        jwtWhitelistRepository.deleteByToken(token);
    }


    public String refreshJWT(String token) {
        JWTWhitelistToken jwtWhitelistToken = jwtWhitelistRepository.findByToken(token.substring(7)).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token not found"));

        Account account = jwtWhitelistToken.getAccount();
        jwtWhitelistRepository.delete(jwtWhitelistToken);
        jwtWhitelistRepository.flush();

        return jwtService.generateToken(new HashMap<>(), account);
    }


}
