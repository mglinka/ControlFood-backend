package com.project.mpa.dto.converter;


import com.project.mpa.dto.GetAllergenDTO;
import com.project.mpa.entity.allergy.Allergen;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AllergenDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    public GetAllergenDTO toAllergenDTO (Allergen allergen) {

        GetAllergenDTO getAllergenDTO = modelMapper.map(allergen, GetAllergenDTO.class);
        return getAllergenDTO;
    }


    public List<GetAllergenDTO> allergenDTOList(List<Allergen> allergens) {
        return allergens.stream().map(this::toAllergenDTO).toList();
    }
}
