package com.project.mopa.dto.product;

import com.project.entity.allergy.AllergenType;
import lombok.Data;

@Data
public class UpdateAllergenDTO {

    private String name;
    private AllergenType type;
}
