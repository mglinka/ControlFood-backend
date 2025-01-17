package pl.lodz.pl.it.mopa.dto;

import pl.lodz.pl.it.entity.allergy.AllergenType;
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
