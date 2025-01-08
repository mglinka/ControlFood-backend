package com.project.mok.controller;

import com.project.dto.account.AccountDTO;
import com.project.dto.account.GetAccountDTO;
import com.project.dto.account.RoleDTO;
import com.project.dto.password.RequestChangePassword;
import com.project.dto.converter.AccountDTOConverter;
import com.project.dto.update.UpdateAccountDataDTO;
import com.project.entity.Account;
import com.project.mok.service.AccountService;
import com.project.utils.ETagBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Validated
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/account/{id}")
    public ResponseEntity<GetAccountDTO> getAccountById(@PathVariable UUID id){

        GetAccountDTO getAccountDTO = accountDTOConverter.toAccountDto(service.getAccountById(id));
        Account account = service.getAccountById(id);
        String eTag = ETagBuilder.buildETag(account.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.ETAG,eTag)
                .body(getAccountDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/user-data/{id}")
    public ResponseEntity<AccountDTO> modifyAccount(
            @Valid @RequestBody UpdateAccountDataDTO updateAccountDataDTO, @PathVariable UUID id
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountDTOConverter.toAccountPersonalDTO(
                service.updateAccountData(accountDTOConverter.toAccount(updateAccountDataDTO), id)));
    }



//    @DeleteMapping("/account/{id}/delete")
//    public ResponseEntity<?> deleteAccount(@PathVariable("id") UUID id) {
//        service.deleteAccount(id);
//        return ResponseEntity.noContent().build();
//    }



    @PatchMapping("/account/changePassword")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody RequestChangePassword request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SPECIALIST', 'ROLE_USER')")
    @GetMapping("/roles")
    public List<RoleDTO> getAllRoles(){
        List<RoleDTO> getRolesDTO = accountDTOConverter.roleDtoList(service.getAllRoles());
        return ResponseEntity.status(HttpStatus.OK).body(getRolesDTO).getBody();

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/changeRole")
    public ResponseEntity<?> changeRole(@RequestParam UUID accountId, @RequestParam UUID roleId){
        service.changeRole(accountId, roleId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/account/enableAccount")
    public ResponseEntity<?> enableAccount(UUID id) {
        service.enableAccount(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/account/disableAccount")
    public ResponseEntity<?> disableAccount(UUID id) {
        service.disableAccount(id);
        return ResponseEntity.ok().build();
    }


}
