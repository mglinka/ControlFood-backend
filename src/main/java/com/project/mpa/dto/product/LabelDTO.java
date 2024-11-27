package com.project.mpa.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class LabelDTO {
//    @NotBlank(message = "Storage information cannot be blank")
//    @Size(max = 255, message = "Storage information must be 255 characters or less")
    private String storage;

//    @NotBlank(message = "Durability information cannot be blank")
//    @Size(max = 255, message = "Durability information must be 255 characters or less")
    private String durability;

//    @Size(max = 255, message = "Instructions after opening must be 255 characters or less")
    private String instructionsAfterOpening;

//    @Size(max = 255, message = "Preparation instructions must be 255 characters or less")
    private String preparation;

//    @Pattern(
//            regexp = "^([A-Za-z\\s]+)(,[\\s]*[A-Za-z\\s]+)*$",
//            message = "Allergens must be a list of words separated by commas (e.g., 'Milk, Eggs, Peanuts')"
//    )
//    @Size(max = 255, message = "Allergens information must be 255 characters or less")
    private String allergens;
    private String image;
}
