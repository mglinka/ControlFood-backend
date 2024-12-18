package com.project.controller;

import com.project.dto.auth.AuthenticationRequest;
import com.project.dto.auth.AuthenticationResponse;
import com.project.dto.auth.RegisterRequest;
import com.project.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.transform.SourceLocator;


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
    public ResponseEntity<String> authenticate (@RequestBody AuthenticationRequest request){
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

        System.out.println("Bartek"+ idToken);

        AuthenticationResponse response = service.authenticateWithGoogle(idToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/amazon/redirect")
    public ResponseEntity<AuthenticationResponse> amazonLogin(@RequestBody String token) {


        System.out.println("Bartek"+ token);
        AuthenticationResponse response = service.authenticateWithAmazon(token);
        return ResponseEntity.ok(response);
    }












}
