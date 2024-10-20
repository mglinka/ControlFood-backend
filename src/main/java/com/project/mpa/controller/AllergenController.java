package com.project.mpa.controller;

import com.project.exception.abstract_exception.AppException;
import com.project.mpa.dto.GetAllergenDTO;
import com.project.mpa.dto.converter.AllergenDTOConverter;
import com.project.mpa.dto.CreateAllergenDTO;
import com.project.mpa.entity.allergy.Allergen;
import com.project.mpa.service.AllergenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<GetAllergenDTO> addAllergen(@RequestBody CreateAllergenDTO createAllergenDTO) {
        // Assuming you have a service to handle the logic
        Allergen allergen = allergenService.createAllergen(createAllergenDTO);

        // Return the created allergen in the response
        return ResponseEntity.status(HttpStatus.CREATED).body(allergenDTOConverter.toAllergenDTO(allergen));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeAllergen(@PathVariable UUID id) throws AppException {

        allergenService.deleteAllergenById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Allergen removed successfully.");
    }

}
