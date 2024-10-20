package com.project.mpa.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAllergyProfileDTO {

    @NotNull(message = "Account ID cannot be null")
    private String accountId; // Assuming account ID is represented as a String (e.g., UUID)

    @NotNull(message = "Allergens cannot be null")
    private List<AllergenIntensityDTO> allergens; // List of allergens with intensity

}
