package pl.lodz.pl.it.mopa.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAllergyProfileSchemaDTO {

    private UUID schema_id;
    @NotNull
    private String name;

    @NotNull(message = "Allergens cannot be null")
    private List<AllergenDTO> allergens;

}