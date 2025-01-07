package com.project.mopa.controller;


import com.project.mopa.dto.CreateAllergyProfileSchemaDTO;
import com.project.mopa.dto.GetAllergyProfileSchemaDTO;
import com.project.mopa.dto.UpdateAllergyProfileSchemaDTO;
import com.project.mopa.dto.converter.AllergyProfileDTOConverter;
import com.project.mopa.service.AllergyProfileSchemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/allergy-profile-schemas")
public class AllergyProfileSchemaController {

    private final AllergyProfileSchemaService allergyProfileSchemaService;
    private final AllergyProfileDTOConverter allergyProfileDTOConverter;

    @GetMapping
    public List<GetAllergyProfileSchemaDTO> getAllProfilesSchemas() {
        List<GetAllergyProfileSchemaDTO> profiles = allergyProfileDTOConverter.allergyProfileSchemaDtoList(allergyProfileSchemaService.getAllAllergyProfile());
        return ResponseEntity.status(HttpStatus.OK).body(profiles).getBody();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetAllergyProfileSchemaDTO> getAllergyProfileSchemaById (@PathVariable UUID id){
        GetAllergyProfileSchemaDTO getAllergyProfileSchemaDTO = allergyProfileDTOConverter.toAllergyProfileSchemaDTO(allergyProfileSchemaService.getAllergyProfileSchemaById(id));
        return ResponseEntity.status(HttpStatus.FOUND).body(getAllergyProfileSchemaDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAllergyProfileSchema (@RequestBody CreateAllergyProfileSchemaDTO createAllergyProfileSchemaDTO){
        allergyProfileSchemaService.createProfile(createAllergyProfileSchemaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editAllergyProfileSchema (@RequestBody UpdateAllergyProfileSchemaDTO updateAllergyProfileSchemaDTO){
        allergyProfileSchemaService.editProfile(updateAllergyProfileSchemaDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAllergyProfileSchema (@PathVariable UUID id){
        allergyProfileSchemaService.deleteAllergyProfileSchema(id);
        return ResponseEntity.ok().build();
    }

}
