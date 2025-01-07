package com.project.mopa.dto;

import com.project.entity.allergy.AllergenType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class GetAllergenDTO {

    private UUID allergen_id;

    private String name;

    private AllergenType allergenType;

}
