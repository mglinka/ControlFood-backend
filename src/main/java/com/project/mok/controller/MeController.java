package com.project.mok.controller;

import com.project.dto.converter.AccountDTOConverter;
import com.project.dto.password.RequestChangePassword;
import com.project.dto.update.UpdateAccountDataDTO;
import com.project.mok.service.MeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Validated
@RestController
@RequestMapping("api/v1/me")
@RequiredArgsConstructor
public class MeController {
    private final MeService meService;
    private final AccountDTOConverter accountDTOConverter;

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody RequestChangePassword request,
            Principal connectedUser){
        meService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateInfo")
    public ResponseEntity<?> updateInfo(@Valid @RequestBody UpdateAccountDataDTO updateAccountDataDTO){
        meService.updateInfo(updateAccountDataDTO);
        return ResponseEntity.ok().build();
    }
}