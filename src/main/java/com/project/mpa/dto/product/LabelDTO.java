package com.project.mpa.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class LabelDTO {
    private String storage;
    private String durability;
    private String instructionsAfterOpening;
    private String preparation;
    private String allergens;
    //private byte [] image;
}
