package com.project.mok.service;

import com.project.auth.dto.AuthenticationResponse;
import com.project.dto.account.CreateAccountDTO;
import com.project.dto.account.GetAccountDTO;
import com.project.dto.password.RequestChangePassword;
import com.project.entity.Account;
import com.project.entity.AccountConfirmation;
import com.project.entity.Role;
import com.project.mok.repository.AccountRepository;
import com.project.auth.repository.RoleRepository;
import com.project.utils.ETagBuilder;
import com.project.utils.TokenGenerator;
import com.project.utils._enum.AccountRoleEnum;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
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
import java.util.Optional;
import java.util.UUID;

import static com.project.utils.Utils.calculateExpirationDate;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Account> getAllAccounts(){

        return accountRepository.findAll();
    }


    public Account getAccountById(UUID id) {

        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
    }

    public void deleteAccount(UUID id) {

        accountRepository.delete(getAccountById(id));
    }

    @Transactional
    public Account updateAccountData(Account accountData, UUID id) {

        Account accountToUpdate = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Konto nie zostało znalezione"));


        accountToUpdate.setEmail(accountData.getEmail());
        accountToUpdate.setFirstName(accountData.getFirstName());
        accountToUpdate.setLastName(accountData.getLastName());


        return accountRepository.saveAndFlush(accountToUpdate);
    }

    @Transactional
    public void changePassword(RequestChangePassword request, Principal connectedUser) {

        var account = (Account) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
    }

    @Transactional
    public void changeRole (UUID accountId, UUID roleId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        if(account.getRole() != null && account.getRole().equals(role)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Konto już posiada ten poziom dostępu");
        }

        account.setRole(role);
        accountRepository.save(account);
    }

    @Transactional
    public void enableAccount(UUID id) {
        Account account = accountRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if(account.getEnabled()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is already enabled");
        }
        account.setEnabled(true);
        accountRepository.saveAndFlush(account);

    }

    @Transactional
    public void disableAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (!account.getEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is already disabled");
        }

        account.setEnabled(false);
        accountRepository.saveAndFlush(account);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public void createAccount(CreateAccountDTO createAccount) {
        try {
            System.out.println("Start");
            System.out.println(createAccount.getRole());

            Role role = roleRepository.findByName(createAccount.getRole())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

            System.out.println("rola"+ role);
            var account = Account.builder()
                    .firstName(createAccount.getFirstName())
                    .lastName(createAccount.getLastName())
                    .email(createAccount.getEmail())
                    .password(passwordEncoder.encode(createAccount.getPassword()))
                    .role(role)
                    .enabled(true)
                    .build();

            System.out.println("Po accoountBuilder");
            //            var randString = TokenGenerator.generateToken();
//
//            var expirationHours = 24;
//            var expirationDate = calculateExpirationDate(expirationHours);
//            var newAccountConfirmation = new AccountConfirmation(randString, account, expirationDate);


//            mailService.sendEmailToVerifyAccount(savedAccount, randString);

            accountRepository.saveAndFlush(account);

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ten email jest już zajęty");
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Niepoprawnie wprowadzone dane");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Niespodziewany błąd");
        }
    }
}
