package com.project.mopa.dto.product;

import com.project.mopa.entity.allergy.AllergenType;
import lombok.Data;

@Data
public class UpdateAllergenDTO {

    private String name;
    private AllergenType type;
}
