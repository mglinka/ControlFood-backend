package com.project.mpa.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class RatingDTO {

    private String groupName;
    private String name;
    private List<String> products;
}
