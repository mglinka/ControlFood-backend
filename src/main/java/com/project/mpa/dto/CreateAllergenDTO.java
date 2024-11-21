package com.project.mpa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAllergenDTO {

    @NotBlank(message = "Allergen name is required.")
    @Size(max = 50, message = "Allergen name cannot exceed 50 characters.")
    @Pattern(regexp = "^[A-Za-ząćęłńóśżźĄĆĘŁŃÓŚŻŹ\\s]+$", message = "Allergen name can only contain letters (including Polish characters) and spaces.")
    private String name;

}
