package com.project.mopa.dto.converter;

import com.project.mopa.dto.*;
import com.project.mopa.entity.allergy.Allergen;
import com.project.mopa.entity.allergy.AllergyProfile;
import com.project.mopa.entity.allergy.AllergyProfileSchema;
import com.project.mopa.entity.allergy.ProfileAllergen;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AllergyProfileDTOConverter {

    @Autowired
    private ModelMapper modelMapper;


    public GetAllergyProfileDTO toAllergyProfileDTO(AllergyProfile allergyProfile) {
        GetAllergyProfileDTO getAllergyProfileDTO = modelMapper.map(allergyProfile, GetAllergyProfileDTO.class);

        List<GetAllergenIntensityDTO> allergenIntensityDTOs = allergyProfile.getProfileAllergens().stream()
                .map(this::convertProfileAllergenToGetAllergenIntensityDTO) // Updated method for intensity
                .collect(Collectors.toList());

        getAllergyProfileDTO.setAllergens(allergenIntensityDTOs);
        return getAllergyProfileDTO;
    }

    public GetAllergyProfileSchemaDTO toAllergyProfileSchemaDTO(AllergyProfileSchema allergyProfileSchema) {
        GetAllergyProfileSchemaDTO getAllergyProfileSchemaDTO = modelMapper.map(allergyProfileSchema, GetAllergyProfileSchemaDTO.class);

        List<GetAllergenDTO> allergenDTOs = allergyProfileSchema.getAllergens().stream()
                .map(allergen -> modelMapper.map(allergen, GetAllergenDTO.class)) // Map each Allergen to GetAllergenDTO
                .collect(Collectors.toList());

        getAllergyProfileSchemaDTO.setAllergens(allergenDTOs);

        return getAllergyProfileSchemaDTO;
    }

    /**
     * Convert ProfileAllergen to GetAllergenIntensityDTO.
     *
     * @param profileAllergen the ProfileAllergen to convert
     * @return converted GetAllergenIntensityDTO
     */
    private GetAllergenIntensityDTO convertProfileAllergenToGetAllergenIntensityDTO(ProfileAllergen profileAllergen) {
        Allergen allergen = profileAllergen.getAllergen(); // Assume ProfileAllergen has a method to get Allergen
        String intensity = profileAllergen.getIntensity(); // Assuming ProfileAllergen has a method to get intensity

        return GetAllergenIntensityDTO.builder()
                .allergen_id(allergen.getAllergen_id()) // Assuming Allergen has a method to get ID
                .name(allergen.getName()) // Assuming Allergen has a method to get name
                .intensity(intensity) // Map intensity from ProfileAllergen
                .type(allergen.getType())
                .build();
    }

    public List<GetAllergyProfileDTO> allergyProfileDtoList(List<AllergyProfile> accounts) {
        return accounts.stream().map(this::toAllergyProfileDTO).toList();
    }

    public List<GetAllergyProfileSchemaDTO> allergyProfileSchemaDtoList(List<AllergyProfileSchema> allergyProfileSchemaList) {
        return allergyProfileSchemaList.stream().map(this::toAllergyProfileSchemaDTO).toList();
    }


}
