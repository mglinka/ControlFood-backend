package com.project.mopa.dto;

import com.project.mopa.entity.allergy.AllergenType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllergenIntensityDTO {

    private UUID allergen_id;
    private String name;
    private String intensity;
    private AllergenType type;
}
