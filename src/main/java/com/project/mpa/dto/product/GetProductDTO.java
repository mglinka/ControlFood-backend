package com.project.mpa.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
}
