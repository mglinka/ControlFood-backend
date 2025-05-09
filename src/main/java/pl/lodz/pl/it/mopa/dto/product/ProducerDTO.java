package pl.lodz.pl.it.mopa.dto.product;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ProducerDTO {

    @NotBlank(message = "Producer name cannot be blank")
    @Size(max = 100, message = "Producer name must be 100 characters or less")
    private String name;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 255, message = "Address must be 255 characters or less")
    private String address;

    @Min(value = 1, message = "Country code must be a positive integer")
    @Max(value = 999, message = "Country code must be a valid ISO country code (1-999)")
    private int countryCode;

    @Email
    private String contact;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "NIP must be exactly 10 digits"
    )
    private String nip;

    @Min(value = 0, message = "RMSD must be greater than or equal to 0")
    @Max(value = 3, message = "RMSD must be less than or equal to 3")
    private int RMSD;
}
