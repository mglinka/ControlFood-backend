package com.project.mok.service;

import com.project.dto.password.RequestChangePassword;
import com.project.entity.Account;
import com.project.exception.abstract_exception.AppException;
import com.project.exception.mok.AccountNotFoundException;
import com.project.mok.repository.AccountMokRepository;
import com.project.utils.messages.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MeService {

    private final AccountMokRepository accountMokRepository;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void changePassword(
            RequestChangePassword request,
            Principal connectedUser) {
        var account = (Account) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())){
            throw new IllegalStateException("Wrong password");
        }
        if(!request.getNewPassword().equals((request.getConfirmationPassword()))){
            throw new IllegalStateException("Passwords are not the same");
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountMokRepository.save(account);



    }

}