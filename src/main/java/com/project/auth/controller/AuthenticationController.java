package com.project.auth.controller;

import com.project.aspects.LoggerInterceptor;
import com.project.auth.service.AuthenticationService;
import com.project.auth.dto.AuthenticationRequest;
import com.project.auth.dto.AuthenticationResponse;
import com.project.auth.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (@Valid @RequestBody RegisterRequest request){

        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @LoggerInterceptor
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate (@Valid @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/verify-account/{token}")
    public ResponseEntity<?> verifyAccount(@PathVariable String token) {
        service.verifyAccount(token);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        SecurityContextHolder.clearContext();
        if(authentication == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body("");

    }

    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.status(HttpStatus.OK).body(service.refreshJWT(token));
    }

    @PostMapping("/google/redirect")
    public ResponseEntity<AuthenticationResponse> googleLogin(@RequestBody String idToken) {

        AuthenticationResponse response = service.authenticateWithGoogle(idToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/amazon/redirect")
    public ResponseEntity<AuthenticationResponse> amazonLogin(@RequestBody String token) {

        AuthenticationResponse response = service.authenticateWithAmazon(token);
        return ResponseEntity.ok(response);
    }












}
