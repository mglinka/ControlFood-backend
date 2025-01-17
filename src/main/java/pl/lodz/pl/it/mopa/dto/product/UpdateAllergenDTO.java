package pl.lodz.pl.it.mopa.dto.product;

import pl.lodz.pl.it.entity.allergy.AllergenType;
import lombok.Data;

@Data
public class UpdateAllergenDTO {

    private String name;
    private AllergenType type;
}
