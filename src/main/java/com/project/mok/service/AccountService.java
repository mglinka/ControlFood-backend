package com.project.mok.service;

import com.project.dto.password.RequestChangePassword;
import com.project.entity.Account;
import com.project.entity.Role;
import com.project.mok.repository.AccountRepository;
import com.project.auth.repository.RoleRepository;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Account> getAllAccounts(){

        return repository.findAll();
    }


    public Account getAccountById(UUID id) {

        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
    }

    public void deleteAccount(UUID id) {

        repository.delete(getAccountById(id));
    }

    @Transactional
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

        accountToUpdate.setFirstName(accountData.getFirstName());
        accountToUpdate.setLastName(accountData.getLastName());


        return repository.saveAndFlush(accountToUpdate);
    }

    @Transactional
    public void changePassword(RequestChangePassword request, Principal connectedUser) {

        var account = (Account) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(account);
    }

    @Transactional
    public void changeRole (UUID accountId, UUID roleId) {
        Account account = repository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        if(account.getRole() != null && account.getRole().equals(role)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account already has this access level");
        }

        account.setRole(role);
        repository.save(account);
    }

    @Transactional
    public void enableAccount(UUID id) {
        Account account = repository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if(account.getEnabled()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is already enabled");
        }
        account.setEnabled(true);
        repository.saveAndFlush(account);

    }

    @Transactional
    public void disableAccount(UUID id) {
        Account account = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (!account.getEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is already disabled");
        }

        account.setEnabled(false);
        repository.saveAndFlush(account);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
