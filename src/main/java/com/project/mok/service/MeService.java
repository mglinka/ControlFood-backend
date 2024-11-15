package com.project.mok.service;

import com.project.dto.password.RequestChangePassword;
import com.project.dto.update.UpdateAccountDataDTO;
import com.project.entity.Account;
import com.project.mok.repository.AccountMokRepository;
import com.project.mok.repository.AccountRepository;
import com.project.utils.ETagBuilder;
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

        if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");
        }
        if(!request.getNewPassword().equals((request.getConfirmationPassword()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords are not the same");
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);



    }

    public Account updateInfo(UpdateAccountDataDTO accountData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();
        System.out.println(principal);
        Account account = (Account) authentication.getPrincipal();
        Account accountToUpdate = accountRepository.findById(account.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));



        System.out.println(accountData.getFirstName());
        accountToUpdate.setFirstName(accountData.getFirstName());
        System.out.println(accountToUpdate.getFirstName());

        System.out.println(accountData.getLastName());
        accountToUpdate.setLastName(accountData.getLastName());
        System.out.println(accountToUpdate.getLastName());


        return accountRepository.saveAndFlush(accountToUpdate);

    }
}