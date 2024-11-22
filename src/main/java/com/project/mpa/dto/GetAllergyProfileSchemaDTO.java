package com.project.mpa.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GetAllergyProfileSchemaDTO {

    private String name;
    private UUID schema_id;
    private List<GetAllergenDTO> allergens;
}
