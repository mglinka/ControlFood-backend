package com.project.mpa.controller;

import com.project.dto.account.GetAccountDTO;
import com.project.entity.Account;
import com.project.exception.abstract_exception.AppException;
import com.project.mpa.dto.CreateAllergyProfileDTO;
import com.project.mpa.dto.GetAllergenDTO;
import com.project.mpa.dto.GetAllergyProfileDTO;
import com.project.mpa.dto.UpdateAllergyProfileDTO;
import com.project.mpa.dto.converter.AllergyProfileDTOConverter;
import com.project.mpa.entity.allergy.AllergyProfile;
import com.project.mpa.service.AllergyProfileService;
import com.project.utils.ETagBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/allergy-profiles") // Ścieżka bazowa dla API
public class AllergyProfileController {

    private final AllergyProfileService allergyProfileService;
    private final AllergyProfileDTOConverter allergyProfileDTOConverter;

    @GetMapping("/{id}")
    public ResponseEntity<GetAllergyProfileDTO> getAllergyProfileById(@PathVariable UUID id){

        GetAllergyProfileDTO getAllergyProfileDTO = allergyProfileDTOConverter.toAllergyProfileDTO(allergyProfileService.getAllergyProfileById(id));
        System.out.println("AAA"+getAllergyProfileDTO.getAllergens());//puste

        AllergyProfile allergyProfile = allergyProfileService.getAllergyProfileById(id);

        String eTag = ETagBuilder.buildETag(allergyProfile.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.ETAG,eTag)
                .body(getAllergyProfileDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<GetAllergyProfileDTO> createProfile(@RequestBody CreateAllergyProfileDTO createAllergyProfileDTO) {
        AllergyProfile savedProfile = allergyProfileService.createProfile(createAllergyProfileDTO);

        System.out.println("1.Kontroler create: "+ savedProfile.getProfile_id() +"  i "+ savedProfile.getProfileAllergens());
        System.out.println("1.Kontroler create: ");
        return ResponseEntity.status(HttpStatus.CREATED).body(allergyProfileDTOConverter.toAllergyProfileDTO(savedProfile));
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<GetAllergyProfileDTO> updateAllergyProfile(@PathVariable UUID id, @RequestBody UpdateAllergyProfileDTO updateAllergyProfileDTO) throws AppException {
        AllergyProfile updatedProfile = allergyProfileService.updateAllergyProfile(id, updateAllergyProfileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(allergyProfileDTOConverter.toAllergyProfileDTO(updatedProfile));
    }

    // Pobieranie wszystkich profili alergii
    @GetMapping
    public List<GetAllergyProfileDTO> getAllProfiles() {
        List<GetAllergyProfileDTO> profiles = allergyProfileDTOConverter.allergyProfileDtoList(allergyProfileService.getAllAllergyProfile());
        return ResponseEntity.status(HttpStatus.OK).body(profiles).getBody();
    }
//
//    // Aktualizacja profilu alergii
//    @PutMapping("/{id}")
//    public ResponseEntity<AllergyProfile> updateProfile(@PathVariable UUID id, @RequestBody AllergyProfile allergyProfile) {
//        AllergyProfile updatedProfile = allergyProfileService.updateProfile(id, allergyProfile);
//        if (updatedProfile != null) {
//            return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    // Usuwanie profilu alergii
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
//        if (allergyProfileService.deleteProfile(id)) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}