package com.project.mpa.dto;

import com.project.mpa.entity.allergy.AllergenType;
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
