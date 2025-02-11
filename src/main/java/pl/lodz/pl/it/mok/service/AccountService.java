package pl.lodz.pl.it.mok.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import pl.lodz.pl.it.dto.account.CreateAccountDTO;
import pl.lodz.pl.it.entity.Account;
import pl.lodz.pl.it.entity.Role;
import pl.lodz.pl.it.mok.repository.AccountRepository;
import pl.lodz.pl.it.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

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

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
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
