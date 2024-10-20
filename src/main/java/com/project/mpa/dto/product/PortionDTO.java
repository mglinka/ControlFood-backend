package com.project.mpa.dto.product;

import com.project.mpa.entity.Unit;
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
