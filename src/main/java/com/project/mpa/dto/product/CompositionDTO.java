package com.project.mpa.dto.product;

import com.project.mpa.entity.Addition;
import com.project.mpa.entity.Flavour;
import com.project.mpa.entity.Ingredient;
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
