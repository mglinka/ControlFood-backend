package com.project.mpa.controller;

import com.project.mpa.dto.GetAllergenDTO;
import com.project.mpa.dto.converter.AllergenDTOConverter;
import com.project.mpa.dto.CreateAllergenDTO;
import com.project.mpa.dto.product.UpdateAllergenDTO;
import com.project.mpa.entity.allergy.Allergen;
import com.project.mpa.service.AllergenService;
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
        // Assuming you have a service to handle the logic
        Allergen allergen = allergenService.createAllergen(createAllergenDTO);

        // Return the created allergen in the response
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
