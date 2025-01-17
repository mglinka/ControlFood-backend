package pl.lodz.pl.it.mopa.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class NutritionalValueNameDTO {
    private NutritionalValueGroupDTO group;

    @NotBlank
    @Schema(example = "Witamina C")
    private String name;
}
