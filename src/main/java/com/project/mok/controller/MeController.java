package com.project.mok.controller;

import com.project.dto.converter.AccountDTOConverter;
import com.project.dto.password.RequestChangePassword;
import com.project.mok.service.MeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/me")
@RequiredArgsConstructor
public class MeController {
    private final MeService meService;
    private final AccountDTOConverter accountDTOConverter;

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody RequestChangePassword request,
            Principal connectedUser){
        meService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}