package com.project.mopa.dto;

import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    private List<UUID> schema_ids;
//    @NotNull
//    private List<AllergenDTO> allergens;

    private String intensity;
}
