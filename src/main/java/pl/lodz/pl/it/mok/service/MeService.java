package pl.lodz.pl.it.mok.service;

import pl.lodz.pl.it.dto.password.RequestChangePassword;
import pl.lodz.pl.it.dto.update.UpdateAccountDataDTO;
import pl.lodz.pl.it.entity.Account;
import pl.lodz.pl.it.mok.repository.AccountRepository;
import pl.lodz.pl.it.utils.ETagBuilder;
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
    public void updateInfo(UpdateAccountDataDTO accountData, String eTag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        try {
            Account accountToUpdate = accountRepository.findById(account.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

            System.out.println("Start Service");
            System.out.println("Received eTag: " + eTag);
            System.out.println("Account version (as string): " + String.valueOf(accountToUpdate.getVersion()));  // Log the account version

            String expectedETag = ETagBuilder.buildETag(String.valueOf(accountToUpdate.getVersion()));
            System.out.println("Generated expected eTag: " + expectedETag);

            boolean isValidETag = ETagBuilder.isETagValid(eTag, String.valueOf(accountToUpdate.getVersion()));
            System.out.println("Is the eTag valid? " + isValidETag);

            if (!isValidETag) {
                System.out.println("Conflict: eTag mismatch detected");
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Nieaktualne dane");
            }

             accountToUpdate.setFirstName(accountData.getFirstName());
            accountToUpdate.setLastName(accountData.getLastName());

            accountRepository.saveAndFlush(accountToUpdate);

        } catch (OptimisticLockException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data conflict occurred. Please retry.");
        }
    }


}