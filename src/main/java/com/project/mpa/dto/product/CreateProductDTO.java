package com.project.mpa.dto.product;

import com.project.mpa.entity.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@RequiredArgsConstructor
@Data
public class CreateProductDTO {

    @NotNull
    @Pattern(regexp = "^[0-9]{13}$" , message = "EAN must be exactly 13 digits")
    private String ean;

    @NotNull
    private ProducerDTO producerDTO;


    private String productName;
    private String productDescription;
    private int productQuantity;
    private UnitDTO unitDTO;
    private PackageTypeDTO packageTypeDTO;
    private String country;
    private CompositionDTO compositionDTO;
    private Set<NutritionalIndexDTO> nutritionalIndexDTOS;
    private Set<ProductIndexDTO> productIndexDTOS;
    private LabelDTO labelDTO;
    private PortionDTO portionDTO;
    private Set<RatingDTO> ratingDTOS;
    private List<NutritionalValueDTO> nutritionalValueDTOS;


}
