package com.project.auth.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {


    @NotBlank(message = "Email nie może być pusty")
    @Email(message = "Nieprawidłowy format adresu email")
    private String email;

    @NotBlank(message = "Hasło nie może być puste")
    @Size(min = 8, message = "Hasło musi mieć co najmniej 8 znaków")
    @Pattern(regexp = ".*[A-Z].*", message = "Hasło musi zawierać co najmniej jedną wielką literę")
    @Pattern(regexp = ".*[0-9].*", message = "Hasło musi zawierać co najmniej jedną cyfrę")
    @Pattern(regexp = ".*[\\W_].*", message = "Hasło musi zawierać co najmniej jeden znak specjalny")
    private String password;
}
