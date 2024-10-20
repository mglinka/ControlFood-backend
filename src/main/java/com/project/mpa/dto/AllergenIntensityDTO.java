package com.project.mpa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllergenIntensityDTO {

    @NotNull(message = "Allergen ID cannot be null")
    private String allergenId; // The ID of the allergen

    private String intensity; // The intensity value for the allergen
}