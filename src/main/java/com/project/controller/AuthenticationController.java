package com.project.controller;

import com.project.dto.auth.AuthenticationRequest;
import com.project.dto.auth.AuthenticationResponse;
import com.project.dto.auth.RegisterRequest;
import com.project.exception.abstract_exception.AppException;
import com.project.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody RegisterRequest request){
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate (@RequestBody AuthenticationRequest request) throws AppException{

        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/verify-account/{token}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> verifyAccount(@PathVariable String token)
            throws AppException {
        service.verifyAccount(token);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('ROLE_PARTICIPANT', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        SecurityContextHolder.clearContext();
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body("sss");

    }








}
