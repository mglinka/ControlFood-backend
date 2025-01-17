package pl.lodz.pl.it.mopa.dto.converter;

import pl.lodz.pl.it.entity.allergy.Allergen;
import pl.lodz.pl.it.entity.allergy.AllergyProfile;
import pl.lodz.pl.it.entity.allergy.AllergyProfileSchema;
import pl.lodz.pl.it.entity.allergy.ProfileAllergen;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.pl.it.mopa.dto.GetAllergenDTO;
import pl.lodz.pl.it.mopa.dto.GetAllergenIntensityDTO;
import pl.lodz.pl.it.mopa.dto.GetAllergyProfileDTO;
import pl.lodz.pl.it.mopa.dto.GetAllergyProfileSchemaDTO;

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
