package com.project.mpa.dto.converter;


import com.project.mpa.dto.AllergenIntensityDTO;
import com.project.mpa.dto.GetAllergenDTO;
import com.project.mpa.dto.GetAllergenIntensityDTO;
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

    public GetAllergenIntensityDTO toAllergenIntensityDTO(Allergen allergen){
        System.out.println("start");
        GetAllergenIntensityDTO allergenIntensityDTO = modelMapper.map(allergen, GetAllergenIntensityDTO.class);
        System.out.println("Marta"+allergen.getType());
        allergenIntensityDTO.setType(allergen.getType());
        return allergenIntensityDTO;
    }


    public List<GetAllergenDTO> allergenDTOList(List<Allergen> allergens) {
        return allergens.stream().map(this::toAllergenDTO).toList();
    }

    public List<GetAllergenIntensityDTO> allergenIntensityDTOS(List<Allergen>allergens){
        return allergens.stream().map(this::toAllergenIntensityDTO).toList();
    }
}
