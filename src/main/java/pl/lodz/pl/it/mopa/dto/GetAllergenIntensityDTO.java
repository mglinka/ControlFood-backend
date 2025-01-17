package pl.lodz.pl.it.mopa.dto;

import pl.lodz.pl.it.entity.allergy.AllergenType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllergenIntensityDTO {

    private UUID allergen_id;
    private String name;
    private String intensity;
    private AllergenType type;
}
