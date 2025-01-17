package pl.lodz.pl.it.mopa.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NutritionalIndexDTO {

    private int indexValue;
    private String legend;
}
