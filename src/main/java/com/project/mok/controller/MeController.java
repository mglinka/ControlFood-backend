package com.project.mok.controller;

import com.project.dto.converter.AccountDTOConverter;
import com.project.mok.service.MeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class MeController {
    private final MeService service;
    private final AccountDTOConverter accountDTOConverter;

//    @GetMapping("/me")
//    public ResponseEntity<String> getMyAccount() {
//        return ResponseEntity.ok("MEACCOUNT");
//    }
//
//    @GetMapping("/accounts")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public List<GetAccountDTO> getAllAccounts() {
//        List<GetAccountDTO> getAccountDTOS = accountDTOConverter.accountDtoList(service);
//        return ResponseEntity.status(HttpStatus.OK).body(getAccountDTOS).getBody();
//    }
}