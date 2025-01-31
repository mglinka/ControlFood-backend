package pl.lodz.pl.it.mopa.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import pl.lodz.pl.it.entity.Account;
import pl.lodz.pl.it.mok.repository.AccountRepository;
import pl.lodz.pl.it.mopa.dto.GetAllergyProfileDTO;
import pl.lodz.pl.it.mopa.dto.UpdateAllergyProfileDTO;
import pl.lodz.pl.it.mopa.dto.converter.AllergyProfileDTOConverter;
import pl.lodz.pl.it.entity.allergy.AllergyProfile;
import pl.lodz.pl.it.mopa.service.AllergyProfileService;
import pl.lodz.pl.it.utils.ETagBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.pl.it.mopa.dto.AssignProfileDTO;
import pl.lodz.pl.it.mopa.dto.CreateAllergyProfileDTO;

import java.util.List;
import java.util.UUID;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/allergy-profiles")
public class AllergyProfileController {

    private final AllergyProfileService allergyProfileService;
    private final AllergyProfileDTOConverter allergyProfileDTOConverter;
    private final AccountRepository accountRepository;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SPECIALIST')")
    @GetMapping("/{id}")
    public ResponseEntity<GetAllergyProfileDTO> getAllergyProfileById(@PathVariable UUID id){

        GetAllergyProfileDTO getAllergyProfileDTO = allergyProfileDTOConverter.toAllergyProfileDTO(allergyProfileService.getAllergyProfileById(id));
        AllergyProfile allergyProfile = allergyProfileService.getAllergyProfileById(id);

        String eTag = ETagBuilder.buildETag(allergyProfile.getVersion().toString());
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.ETAG,eTag)
                .body(getAllergyProfileDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SPECIALIST')")
    @GetMapping("/byAccount/{id}")
    public ResponseEntity<GetAllergyProfileDTO> getAllergyProfileByAccountId(@PathVariable UUID id) {
        System.out.println("Controller");
        AllergyProfile allergyProfile = allergyProfileService.getAllergyProfileByAccountId(id);

        GetAllergyProfileDTO getAllergyProfileDTO = allergyProfileDTOConverter.toAllergyProfileDTO(allergyProfile);

        return ResponseEntity.status(HttpStatus.OK)
                .body(getAllergyProfileDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SPECIALIST')")
    @PostMapping("/create")
    public ResponseEntity<GetAllergyProfileDTO> createProfile(@RequestBody CreateAllergyProfileDTO createAllergyProfileDTO) {
        AllergyProfile savedProfile = allergyProfileService.createProfile(createAllergyProfileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(allergyProfileDTOConverter.toAllergyProfileDTO(savedProfile));
    }



    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SPECIALIST')")
    @PutMapping("/update/{accountId}")
    public ResponseEntity<GetAllergyProfileDTO> updateAllergyProfile( @PathVariable UUID accountId, @RequestBody UpdateAllergyProfileDTO updateAllergyProfileDTO) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        UUID profileId = account.getAllergyProfile().getProfile_id();
        AllergyProfile updatedProfile = allergyProfileService.updateAllergyProfile(profileId, updateAllergyProfileDTO);
        return ResponseEntity.status(HttpStatus.OK).body(allergyProfileDTOConverter.toAllergyProfileDTO(updatedProfile));
    }

    @PreAuthorize("hasRole('ROLE_SPECIALIST')")
    @GetMapping
    public List<GetAllergyProfileDTO> getAllProfiles() {
        List<GetAllergyProfileDTO> profiles = allergyProfileDTOConverter.allergyProfileDtoList(allergyProfileService.getAllAllergyProfile());
        return ResponseEntity.status(HttpStatus.OK).body(profiles).getBody();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_SPECIALIST')")
    @PostMapping("/assignProfile")
    public ResponseEntity<?> assignAllergyProfile(@RequestBody AssignProfileDTO dto){
        allergyProfileService.assignAllergyProfile(dto);
        return ResponseEntity.ok().build();
    }
}