package com.project.mok.service;

import com.project.dto.password.RequestChangePassword;
import com.project.dto.update.UpdateAccountDataDTO;
import com.project.entity.Account;
import com.project.mok.repository.AccountRepository;
import jakarta.persistence.OptimisticLockException;
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
import java.util.Objects;

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
    public void updateInfo(UpdateAccountDataDTO accountData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        try {
            Account accountToUpdate = accountRepository.findById(account.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

            if(!Objects.equals(accountData.getVersion(), account.getVersion())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Optimistic Lock Exception");
            }

             accountToUpdate.setFirstName(accountData.getFirstName());
            accountToUpdate.setLastName(accountData.getLastName());

            accountRepository.saveAndFlush(accountToUpdate);

        } catch (OptimisticLockException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data conflict occurred. Please retry.");
        }
    }


}