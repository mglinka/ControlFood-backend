package com.project.mpa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAllergyProfileSchemaDTO {

    @NotNull
    private String name;

    @NotNull(message = "Allergens cannot be null")
    private List<AllergenDTO> allergens;

}
