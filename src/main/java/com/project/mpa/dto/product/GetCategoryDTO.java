package com.project.mpa.dto.product;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class GetCategoryDTO {

    private UUID id;
    private String name;
}
