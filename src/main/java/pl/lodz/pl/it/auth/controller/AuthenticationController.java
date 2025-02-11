package pl.lodz.pl.it.auth.controller;


import pl.lodz.pl.it.auth.service.AuthenticationService;
import pl.lodz.pl.it.auth.dto.AuthenticationRequest;
import pl.lodz.pl.it.auth.dto.AuthenticationResponse;
import pl.lodz.pl.it.auth.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.pl.it.entity.Account;

import java.util.UUID;


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



    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate (@Valid @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/verify-account/{token}")
    public ResponseEntity<?> verifyAccount(@PathVariable String token) {
        service.verifyAccount(token);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SPECIALIST', 'ROLE_USER')")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Account) {
            Account account = (Account) authentication.getPrincipal();
            UUID accountId = account.getId(); // Pobieramy accountId z obiektu Account

            service.logout(accountId);

            SecurityContextHolder.clearContext();
            return ResponseEntity.ok().body("Successfully logged out."); // 200 OK with success message
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
