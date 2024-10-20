package com.project.mpa.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GetAllergyProfileDTO {

    private UUID profile_id;
    private List<GetAllergenIntensityDTO> allergens;

}
