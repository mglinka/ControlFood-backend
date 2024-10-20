package com.project.mpa.dto.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class NutritionalValueNameDTO {
    private NutritionalValueGroupDTO group;
    private String name;
}
