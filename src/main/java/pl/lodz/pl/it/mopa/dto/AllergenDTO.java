package pl.lodz.pl.it.mopa.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class AllergenDTO {

    private UUID allergen_id;
}
