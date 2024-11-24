package com.project.mpa.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignProfileDTO {

    @NotNull
    private UUID schema_id;

    @NotNull
    private List<AllergenDTO> allergens;

    private String intensity;
}
