package pl.lodz.pl.it.mopa.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateAllergyProfileDTO {
    private List<AllergenIntensityDTO> allergens;
}
