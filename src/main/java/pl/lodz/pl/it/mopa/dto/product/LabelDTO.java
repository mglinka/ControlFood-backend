package pl.lodz.pl.it.mopa.dto.product;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class LabelDTO {

    @Size(max = 255, message = "Storage information must be 255 characters or less")
    private String storage;

    @Size(max = 255, message = "Durability information must be 255 characters or less")
    private String durability;

    @Size(max = 255, message = "Instructions after opening must be 255 characters or less")
    private String instructionsAfterOpening;

    @Size(max = 255, message = "Preparation instructions must be 255 characters or less")
    private String preparation;


    @Size(max = 255, message = "Allergens information must be 255 characters or less")
    private String allergens;
    private String image;

    @Override
    public String toString() {
        return "LabelDTO{" +
                "storage='" + storage + '\'' +
                ", durability='" + durability + '\'' +
                ", instructionsAfterOpening='" + instructionsAfterOpening + '\'' +
                ", preparation='" + preparation + '\'' +
                ", allergens='" + allergens + '\'' +
//                ", image='" + image + '\'' +
                '}';
    }
}
