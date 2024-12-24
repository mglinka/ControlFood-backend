package com.project.mopa.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CompositionDTO {

    private List<IngredientDTO> ingredientDTOS;
    private List<AdditionDTO> additionDTOS;
    private FlavourDTO flavourDTO;
}
