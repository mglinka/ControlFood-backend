package com.project.mok.service;

import com.project.dto.password.RequestChangePassword;
import com.project.entity.Account;
import com.project.mok.repository.AccountRepository;
import com.project.utils.ETagBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<Account> getAllAccounts(){

        return repository.findAll();
    }

    public Account getAccountById(UUID id) {

        final Account account = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        return account;
    }

    public void deleteAccount(UUID id) {

        repository.delete(getAccountById(id));
    }

    public Account updateMyAccountData(Account accountData, String eTag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();
        System.out.println(principal);
        Account account = (Account) authentication.getPrincipal();
        Account accountToUpdate = repository.findById(account.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        System.out.println("ETAG:"+ eTag);
        if (!ETagBuilder.isETagValid(eTag, String.valueOf(accountToUpdate.getVersion()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Optimistic lock exception");
        }

        System.out.println(accountData.getFirstName());
        accountToUpdate.setFirstName(accountData.getFirstName());
        System.out.println(accountToUpdate.getFirstName());

        System.out.println(accountData.getLastName());
        accountToUpdate.setLastName(accountData.getLastName());
        System.out.println(accountToUpdate.getLastName());

        var returnedAccount = repository.saveAndFlush(accountToUpdate);


        return returnedAccount;
    }

    public void changePassword(RequestChangePassword request, Principal connectedUser) {

        var account = (Account) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();


        if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }


        account.setPassword(passwordEncoder.encode(request.getNewPassword()));


        repository.save(account);
    }





}
