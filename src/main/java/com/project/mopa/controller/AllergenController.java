package com.project.mopa.controller;


import com.project.mopa.dto.GetAllergenDTO;
import com.project.mopa.dto.converter.AllergenDTOConverter;
import com.project.mopa.dto.CreateAllergenDTO;
import com.project.mopa.dto.product.UpdateAllergenDTO;
import com.project.entity.allergy.Allergen;
import com.project.mopa.service.AllergenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @GetMapping
    public List<GetAllergenDTO> getAllAllergens() {
        List<GetAllergenDTO> getAllergenDTOS = allergenDTOConverter.allergenDTOList(allergenService.getAllAllergens());

        return ResponseEntity.status(HttpStatus.OK).body(getAllergenDTOS).getBody();
    }

    @PostMapping("/add")
    public ResponseEntity<GetAllergenDTO> addAllergen(@Valid @RequestBody CreateAllergenDTO createAllergenDTO) {
        System.out.println("Controller");
        Allergen allergen = allergenService.createAllergen(createAllergenDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(allergenDTOConverter.toAllergenDTO(allergen));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<GetAllergenDTO> editAllergen(@PathVariable UUID id, @Valid @RequestBody UpdateAllergenDTO updateAllergenDTO){
        Allergen allergen = allergenService.editAllergen(id, updateAllergenDTO);
        return ResponseEntity.status(HttpStatus.OK).body(allergenDTOConverter.toAllergenDTO(allergen));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeAllergen(@PathVariable UUID id){

        allergenService.deleteAllergenById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Allergen removed successfully.");
    }

}
