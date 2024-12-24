package com.project.mok.service;

import com.project.dto.password.RequestChangePassword;
import com.project.dto.update.UpdateAccountDataDTO;
import com.project.entity.Account;
import com.project.mok.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MeService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void changePassword(
            RequestChangePassword request,
            Principal connectedUser) {
        var account = (Account) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();


        if(!request.getNewPassword().equals((request.getConfirmationPassword()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords are not the same");
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);



    }

    @Transactional
    public Account updateInfo(UpdateAccountDataDTO accountData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();
        System.out.println(principal);
        Account account = (Account) authentication.getPrincipal();
        Account accountToUpdate = accountRepository.findById(account.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));


        accountToUpdate.setFirstName(accountData.getFirstName());

        accountToUpdate.setLastName(accountData.getLastName());

        return accountRepository.saveAndFlush(accountToUpdate);

    }
}