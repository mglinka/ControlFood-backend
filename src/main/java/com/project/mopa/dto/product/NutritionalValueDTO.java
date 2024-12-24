package com.project.mopa.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class NutritionalValueDTO {

    private NutritionalValueNameDTO nutritionalValueName;
    private double quantity;
    private UnitDTO unit;
    private double nrv;
}
