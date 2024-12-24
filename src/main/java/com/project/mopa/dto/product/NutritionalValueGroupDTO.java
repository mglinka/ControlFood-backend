package com.project.mopa.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class NutritionalValueGroupDTO {
    @Schema(example = "Błonnik")
    private String groupName;
}
