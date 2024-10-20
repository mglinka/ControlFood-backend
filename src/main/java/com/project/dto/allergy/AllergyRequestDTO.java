package com.project.dto.allergy;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AllergyRequestDTO {

    @NotNull
    @Size(min = 1, max = 255)
    @Schema(description = "Allergy name", example = "Kiwi")
    private String name;
}
