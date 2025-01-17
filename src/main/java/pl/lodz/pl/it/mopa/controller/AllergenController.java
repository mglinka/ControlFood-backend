package pl.lodz.pl.it.mopa.controller;


import pl.lodz.pl.it.mopa.dto.GetAllergenDTO;
import pl.lodz.pl.it.mopa.dto.converter.AllergenDTOConverter;
import pl.lodz.pl.it.mopa.dto.CreateAllergenDTO;
import pl.lodz.pl.it.mopa.dto.product.UpdateAllergenDTO;
import pl.lodz.pl.it.entity.allergy.Allergen;
import pl.lodz.pl.it.mopa.service.AllergenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/allergens")
public class AllergenController {

    private final AllergenService allergenService;
    private final AllergenDTOConverter allergenDTOConverter;


    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_USER')")
    @GetMapping
    public List<GetAllergenDTO> getAllAllergens() {
        List<GetAllergenDTO> getAllergenDTOS = allergenDTOConverter.allergenDTOList(allergenService.getAllAllergens());

        return ResponseEntity.status(HttpStatus.OK).body(getAllergenDTOS).getBody();
    }

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @PostMapping("/add")
    public ResponseEntity<GetAllergenDTO> addAllergen(@Valid @RequestBody CreateAllergenDTO createAllergenDTO) {
        System.out.println("Controller");
        Allergen allergen = allergenService.createAllergen(createAllergenDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(allergenDTOConverter.toAllergenDTO(allergen));
    }

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<GetAllergenDTO> editAllergen(@PathVariable UUID id, @Valid @RequestBody UpdateAllergenDTO updateAllergenDTO){
        Allergen allergen = allergenService.editAllergen(id, updateAllergenDTO);
        return ResponseEntity.status(HttpStatus.OK).body(allergenDTOConverter.toAllergenDTO(allergen));
    }

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeAllergen(@PathVariable UUID id){

        allergenService.deleteAllergenById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Allergen removed successfully.");
    }

}
