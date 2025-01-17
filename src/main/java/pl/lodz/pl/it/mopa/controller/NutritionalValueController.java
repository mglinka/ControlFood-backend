package pl.lodz.pl.it.mopa.controller;

import pl.lodz.pl.it.mopa.dto.converter.ProductDTOConverter;
import pl.lodz.pl.it.mopa.dto.product.NutritionalValueGroupDTO;
import pl.lodz.pl.it.mopa.dto.product.NutritionalValueNameDTO;
import pl.lodz.pl.it.mopa.service.NutritionalValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/nutritional-value")
public class NutritionalValueController {

    private final NutritionalValueService nutritionalValueService;
    private final ProductDTOConverter productDTOConverter;

    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_USER')")
    @GetMapping("/names")
    public List<NutritionalValueNameDTO> getAllNutritionalValueNames() {
        List<NutritionalValueNameDTO> getNutritionalValueNames = productDTOConverter.nutritionalValueNameDTOList(nutritionalValueService.getAllNutritionalValueNames());
        return ResponseEntity.status(HttpStatus.OK).body(getNutritionalValueNames).getBody();
    }

    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_USER')")
    @GetMapping("/group-names")
    public List<NutritionalValueGroupDTO> getAllNutritionalValueGroupNames() {
        List<NutritionalValueGroupDTO> getNutritionalValueGroupNames = productDTOConverter.nutritionalValueGroupDTOList(nutritionalValueService.getAllNutritionalValueGroupNames());
        return ResponseEntity.status(HttpStatus.OK).body(getNutritionalValueGroupNames).getBody();
    }
}
