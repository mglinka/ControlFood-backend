package com.project.mopa.controller;

import com.project.entity.Account;
import com.project.mok.repository.AccountRepository;
import com.project.mopa.dto.*;
import com.project.mopa.dto.converter.AllergyProfileDTOConverter;
import com.project.mopa.entity.allergy.AllergyProfile;
import com.project.mopa.service.AllergyProfileService;
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
@RequestMapping("/api/v1/allergy-profiles")
public class AllergyProfileController {

    private final AllergyProfileService allergyProfileService;
    private final AllergyProfileDTOConverter allergyProfileDTOConverter;
    private final AccountRepository accountRepository;

    @GetMapping("/{id}")
    public ResponseEntity<GetAllergyProfileDTO> getAllergyProfileById(@PathVariable UUID id){

        GetAllergyProfileDTO getAllergyProfileDTO = allergyProfileDTOConverter.toAllergyProfileDTO(allergyProfileService.getAllergyProfileById(id));
        AllergyProfile allergyProfile = allergyProfileService.getAllergyProfileById(id);

        String eTag = ETagBuilder.buildETag(allergyProfile.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.ETAG,eTag)
                .body(getAllergyProfileDTO);
    }

    @GetMapping("/byAccount/{id}")
    public ResponseEntity<GetAllergyProfileDTO> getAllergyProfileByAccountId(@PathVariable UUID id) {
        System.out.println("Controller");
        AllergyProfile allergyProfile = allergyProfileService.getAllergyProfileByAccountId(id);

        GetAllergyProfileDTO getAllergyProfileDTO = allergyProfileDTOConverter.toAllergyProfileDTO(allergyProfile);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getAllergyProfileDTO);
    }


    @PostMapping("/create")
    public ResponseEntity<GetAllergyProfileDTO> createProfile(@RequestBody CreateAllergyProfileDTO createAllergyProfileDTO) {
        AllergyProfile savedProfile = allergyProfileService.createProfile(createAllergyProfileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(allergyProfileDTOConverter.toAllergyProfileDTO(savedProfile));
    }



    @PutMapping("/update/{accountId}")
    public ResponseEntity<GetAllergyProfileDTO> updateAllergyProfile( @PathVariable UUID accountId, @RequestBody UpdateAllergyProfileDTO updateAllergyProfileDTO) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        UUID profileId = account.getAllergyProfile().getProfile_id();
        AllergyProfile updatedProfile = allergyProfileService.updateAllergyProfile(profileId, updateAllergyProfileDTO);
        return ResponseEntity.status(HttpStatus.OK).body(allergyProfileDTOConverter.toAllergyProfileDTO(updatedProfile));
    }

    @GetMapping
    public List<GetAllergyProfileDTO> getAllProfiles() {
        List<GetAllergyProfileDTO> profiles = allergyProfileDTOConverter.allergyProfileDtoList(allergyProfileService.getAllAllergyProfile());
        return ResponseEntity.status(HttpStatus.OK).body(profiles).getBody();
    }

    @PostMapping("/assignProfile")
    public ResponseEntity<?> assignAllergyProfile(@RequestBody AssignProfileDTO dto){
        allergyProfileService.assignAllergyProfile(dto);
        return ResponseEntity.ok().build();
    }
}