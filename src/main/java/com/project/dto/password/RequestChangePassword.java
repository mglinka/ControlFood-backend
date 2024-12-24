package com.project.dto.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class RequestChangePassword {

    @NotBlank(message = "Nowe hasło nie może być puste")
    @Size(min = 8, message = "Nowe hasło musi mieć co najmniej 8 znaków")
    @Pattern(regexp = ".*[A-Z].*", message = "Nowe hasło musi zawierać co najmniej jedną wielką literę")
    @Pattern(regexp = ".*[0-9].*", message = "Nowe hasło musi zawierać co najmniej jedną cyfrę")
    @Pattern(regexp = ".*[\\W_].*", message = "Nowe hasło musi zawierać co najmniej jeden znak specjalny")
    private String newPassword;

    @NotBlank(message = "Potwierdzenie hasła nie może być puste")
    private String confirmationPassword;

}
