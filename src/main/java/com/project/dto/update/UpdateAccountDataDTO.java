package com.project.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAccountDataDTO {

    @NotBlank(message = "Imię nie może być puste")
    @Size(min = 2, max = 32, message = "Imię musi mieć od 2 do 32 znaków")
    private String firstName;

    @NotBlank(message = "Nazwisko nie może być puste")
    @Size(min = 2, max = 64, message = "Nazwisko musi mieć od 2 do 64 znaków")
    private String lastName;

}
