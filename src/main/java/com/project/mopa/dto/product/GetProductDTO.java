package com.project.mopa.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@RequiredArgsConstructor
public class GetProductDTO {


    private UUID id;

    @NotNull
    private Long version;

    private String country;

    @NotBlank
    private String ean;

    private String productDescription;

    @NotBlank
    private String productName;

    @NotNull
    private Integer productQuantity;

    @NotNull
    private UUID compositionId;

    @NotNull
    private UUID labelId;

    @NotNull
    private UUID packageTypeId;

    @NotNull
    private UUID portionId;

    @NotNull
    private UUID producerId;

    @NotNull
    private UUID unitId;

    private LabelDTO labelDTO;

    private CompositionDTO compositionDTO;

    @NotNull
    private List<NutritionalValueDTO> nutritionalValueDTOS;

    private GetCategoryDTO categoryDTO;

}
