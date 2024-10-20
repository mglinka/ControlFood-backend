package com.project.mpa.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllergenIntensityDTO {

    private UUID allergen_id;
    private String name;
    private String intensity;
}
