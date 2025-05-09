package pl.lodz.pl.it.mopa.controller;


import pl.lodz.pl.it.entity.allergy.AllergyProfileSchema;
import pl.lodz.pl.it.mopa.dto.CreateAllergyProfileSchemaDTO;
import pl.lodz.pl.it.mopa.dto.GetAllergyProfileSchemaDTO;
import pl.lodz.pl.it.mopa.dto.UpdateAllergyProfileSchemaDTO;
import pl.lodz.pl.it.mopa.dto.converter.AllergyProfileDTOConverter;
import pl.lodz.pl.it.mopa.service.AllergyProfileSchemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/allergy-profile-schemas")
public class AllergyProfileSchemaController {

    private final AllergyProfileSchemaService allergyProfileSchemaService;
    private final AllergyProfileDTOConverter allergyProfileDTOConverter;

    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_USER')")
    @GetMapping
    public List<GetAllergyProfileSchemaDTO> getAllProfilesSchemas() {
        List<GetAllergyProfileSchemaDTO> profiles = allergyProfileDTOConverter.allergyProfileSchemaDtoList(allergyProfileSchemaService.getAllAllergyProfile());
        return ResponseEntity.status(HttpStatus.OK).body(profiles).getBody();
    }

    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<GetAllergyProfileSchemaDTO> getAllergyProfileSchemaById (@PathVariable UUID id){
        GetAllergyProfileSchemaDTO getAllergyProfileSchemaDTO = allergyProfileDTOConverter.toAllergyProfileSchemaDTO(allergyProfileSchemaService.getAllergyProfileSchemaById(id));
        return ResponseEntity.status(HttpStatus.FOUND).body(getAllergyProfileSchemaDTO);
    }

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @PostMapping("/create")
    public ResponseEntity<UUID> createAllergyProfileSchema (@RequestBody CreateAllergyProfileSchemaDTO createAllergyProfileSchemaDTO){
        AllergyProfileSchema profile = allergyProfileSchemaService.createProfile(
            createAllergyProfileSchemaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(profile.getSchema_id());
    }

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @PutMapping("/edit")
    public ResponseEntity<?> editAllergyProfileSchema (@RequestBody UpdateAllergyProfileSchemaDTO updateAllergyProfileSchemaDTO){
        allergyProfileSchemaService.editProfile(updateAllergyProfileSchemaDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAllergyProfileSchema (@PathVariable UUID id){
        allergyProfileSchemaService.deleteAllergyProfileSchema(id);
        return ResponseEntity.ok().build();
    }

}
