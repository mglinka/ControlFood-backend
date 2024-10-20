package com.project.mpa.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateAllergyProfileDTO {
    private List<GetAllergenDTO> allergens;
}
