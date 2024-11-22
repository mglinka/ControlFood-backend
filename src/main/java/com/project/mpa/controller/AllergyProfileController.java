package com.project.mpa.controller;

import com.project.entity.Account;
import com.project.mok.repository.AccountRepository;
import com.project.mpa.dto.CreateAllergyProfileDTO;
import com.project.mpa.dto.GetAllergyProfileDTO;
import com.project.mpa.dto.UpdateAllergyProfileDTO;
import com.project.mpa.dto.converter.AllergyProfileDTOConverter;
import com.project.mpa.entity.allergy.AllergyProfile;
import com.project.mpa.repository.allergy.AllergyProfileRepository;
import com.project.mpa.service.AllergyProfileService;
import com.project.utils.ETagBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
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
        AllergyProfile allergyProfile = allergyProfileService.getAllergyProfileByAccountId(id);


        String eTag = ETagBuilder.buildETag(allergyProfile.getVersion().toString());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.ETAG, eTag)
                .body(allergyProfileDTOConverter.toAllergyProfileDTO(allergyProfile));
    }


    @PostMapping("/create")
    public ResponseEntity<GetAllergyProfileDTO> createProfile(@RequestBody CreateAllergyProfileDTO createAllergyProfileDTO) {
        System.out.println("Marta" + createAllergyProfileDTO.getAccountId());

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

}