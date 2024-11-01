package com.project.mok.controller;

import com.project.dto.account.GetAccountDTO;
import com.project.dto.account.GetAccountPersonalDTO;
import com.project.dto.password.RequestChangePassword;
import com.project.dto.converter.AccountDTOConverter;
import com.project.dto.update.UpdateAccountDataDTO;
import com.project.entity.Account;
import com.project.exception.abstract_exception.AppException;
import com.project.mok.service.AccountService;
import com.project.utils.ETagBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;
    private final AccountDTOConverter accountDTOConverter;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/accounts")
    public List<GetAccountDTO> getAllAccounts() {
        List<GetAccountDTO> getAccountDTOS = accountDTOConverter.accountDtoList(service.getAllAccounts());
        return ResponseEntity.status(HttpStatus.OK).body(getAccountDTOS).getBody();
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<GetAccountDTO> getAccountById(@PathVariable UUID id){

        GetAccountDTO getAccountDTO = accountDTOConverter.toAccountDto(service.getAccountById(id));
        Account account = service.getAccountById(id);
        String eTag = ETagBuilder.buildETag(account.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.ETAG,eTag)
                .body(getAccountDTO);
    }

    @PutMapping("/user-data")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_PARTICIPANT')")
    public ResponseEntity<GetAccountPersonalDTO> modifyAccountSelf(
            @RequestHeader("If-Match") String eTag,
            @Valid @RequestBody UpdateAccountDataDTO updateAccountDataDTO
    ) throws AppException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountDTOConverter.toAccountPersonalDTO(
                service.updateMyAccountData(accountDTOConverter.toAccount(updateAccountDataDTO), eTag)));
    }



    @DeleteMapping("/account/{id}/delete")
    public ResponseEntity<?> deleteAccount(@PathVariable("id") UUID id) {
        service.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/account/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestBody RequestChangePassword request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }


}
