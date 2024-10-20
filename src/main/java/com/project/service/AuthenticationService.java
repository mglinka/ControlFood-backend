package com.project.service;

import com.project.dto.auth.AuthenticationRequest;
import com.project.dto.auth.AuthenticationResponse;
import com.project.dto.auth.RegisterRequest;
import com.project.entity.Account;
import com.project.entity.AccountConfirmation;
import com.project.exception.abstract_exception.AppException;
import com.project.exception.auth.AccountConfirmationTokenExpiredException;
import com.project.exception.auth.AccountConfirmationTokenNotFoundException;
import com.project.exception.mok.AccountNotFoundException;
import com.project.exception.mok.RoleNotFoundException;
import com.project.repository.AccountConfirmationRepository;
import com.project.mok.repository.AccountRepository;
import com.project.repository.JWTWhitelistRepository;
import com.project.repository.RoleRepository;
import com.project.security.JwtService;
import com.project.utils.RunAs;
import com.project.utils.TokenGenerator;
import com.project.utils._enum.AccountRoleEnum;
import com.project.utils.mail.MailService;
import com.project.utils.messages.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public String authenticate (AuthenticationRequest request) throws AppException{
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
//        String hash = passwordEncoder.encode("jane");
//        System.out.println(hash);
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
    public void verifyAccount(String token)
            throws AppException {
        var accountConfirmation = accountConfirmationRepository.findByToken(token)
                .orElseThrow(() -> new AccountConfirmationTokenNotFoundException(ExceptionMessages.CONFIRMATION_TOKEN_NOT_FOUND));

        if (accountConfirmation.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new AccountConfirmationTokenExpiredException(ExceptionMessages.CONFIRMATION_TOKEN_EXPIRED);
        }

        var accountId = accountConfirmation.getAccount().getId();
        var account = repository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));

        account.setEnabled(true);
        account.addRole(roleRepository.findByName(AccountRoleEnum.ROLE_USER)
               .orElseThrow(() -> new RoleNotFoundException(ExceptionMessages.ROLE_NOT_FOUND)));

        repository.saveAndFlush(account);

        accountConfirmationRepository.delete(accountConfirmation);


        RunAs.runAsSystem(() -> mailService.sendEmailToInformAboutVerification(account));
    }

    public void logout(String token) {
        jwtWhitelistRepository.deleteByToken(token);
    }



}
