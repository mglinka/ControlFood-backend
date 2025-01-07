package com.project.mopa.dto;

import com.project.entity.allergy.AllergenType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllergenIntensityDTO {

    @NotNull(message = "Allergen ID cannot be null")
    private String allergen_id; // The ID of the allergen

    private String name;
    private String intensity; // The intensity value for the allergen

    private AllergenType allergenType;
}