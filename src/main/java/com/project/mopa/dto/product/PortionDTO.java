package com.project.mopa.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PortionDTO {

    private int portionQuantity;
    private UnitDTO unitDTO;

}
