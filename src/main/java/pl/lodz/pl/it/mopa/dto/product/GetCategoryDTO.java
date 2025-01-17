package pl.lodz.pl.it.mopa.dto.product;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class GetCategoryDTO {

    private UUID id;
    private String name;
}
