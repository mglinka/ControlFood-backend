package pl.lodz.pl.it.mopa.dto.converter;


import pl.lodz.pl.it.mopa.dto.GetAllergenDTO;
import pl.lodz.pl.it.mopa.dto.GetAllergenIntensityDTO;
import pl.lodz.pl.it.entity.allergy.Allergen;
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

        return modelMapper.map(allergen, GetAllergenDTO.class);
    }

    public GetAllergenIntensityDTO toAllergenIntensityDTO(Allergen allergen){
        GetAllergenIntensityDTO allergenIntensityDTO = modelMapper.map(allergen, GetAllergenIntensityDTO.class);
        allergenIntensityDTO.setType(allergen.getType());
        return allergenIntensityDTO;
    }


    public List<GetAllergenDTO> allergenDTOList(List<Allergen> allergens) {
        return allergens.stream().map(this::toAllergenDTO).toList();
    }

}
