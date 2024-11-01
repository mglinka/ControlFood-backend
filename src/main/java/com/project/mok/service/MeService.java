package com.project.mok.service;

import com.project.dto.password.RequestChangePassword;
import com.project.entity.Account;
import com.project.mok.repository.AccountMokRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");
        }
        if(!request.getNewPassword().equals((request.getConfirmationPassword()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords are not the same");
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountMokRepository.save(account);



    }

}