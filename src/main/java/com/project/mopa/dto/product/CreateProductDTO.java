package com.project.mopa.dto.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;


@RequiredArgsConstructor
@Data
public class CreateProductDTO {

    @NotNull
    @Pattern(regexp = "^[0-9]{13}$" , message = "EAN must be exactly 13 digits")
    private String ean;


    @NotNull(message = "Product name cannot be null")
    @Size(min = 1, max = 100, message = "Product name must be between 1 and 100 characters")
    private String productName;

    @NotNull
    private GetCategoryDTO category;

    @Size(max = 255, message = "Product description must be 255 characters or less")
    private String productDescription;

    @Min(value = 0, message = "Product quantity must be zero or greater")
    private int productQuantity;

    @NotBlank(message = "Country cannot be blank")
    private String country;

    @Valid
    @NotNull(message = "Unit information is required")
    private UnitDTO unitDTO;

    @Valid
    @NotNull(message = "Producer information is required")
    private ProducerDTO producerDTO;

    @Valid
    @NotNull(message = "Package type information is required")
    private PackageTypeDTO packageTypeDTO;



    @Valid
    private CompositionDTO compositionDTO;

//    @Valid
//    private Set<NutritionalIndexDTO> nutritionalIndexDTOS;

//    @Valid
//    private Set<ProductIndexDTO> productIndexDTOS;

    @Valid
    private LabelDTO labelDTO;

    @Valid
    private PortionDTO portionDTO;

//    @Valid
//    private Set<RatingDTO> ratingDTOS;

    @Valid
//    @NotEmpty(message = "Nutritional values cannot be empty")
    private List<NutritionalValueDTO> nutritionalValueDTOS;


}
