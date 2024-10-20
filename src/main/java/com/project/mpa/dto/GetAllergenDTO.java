package com.project.mpa.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class GetAllergenDTO {

    private UUID allergen_id;

    private String name;

}
