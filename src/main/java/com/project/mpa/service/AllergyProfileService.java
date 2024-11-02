package com.project.mpa.service;


import com.project.entity.Account;
import com.project.mok.repository.AccountRepository;
import com.project.mpa.dto.AllergenIntensityDTO;
import com.project.mpa.dto.CreateAllergyProfileDTO;
import com.project.mpa.dto.UpdateAllergyProfileDTO;
import com.project.mpa.dto.converter.AllergenDTOConverter;
import com.project.mpa.dto.converter.AllergyProfileDTOConverter;
import com.project.mpa.entity.allergy.Allergen;
import com.project.mpa.entity.allergy.AllergyProfile;
import com.project.mpa.entity.allergy.ProfileAllergen;
import com.project.mpa.entity.allergy.ProfileAllergenId;
import com.project.mpa.repository.allergy.AllergenRepository;
import com.project.mpa.repository.allergy.AllergyProfileRepository;
import com.project.mpa.repository.allergy.ProfileAllergenRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AllergyProfileService {

    @PersistenceContext
    EntityManager entityManager;
    private final AllergyProfileRepository allergyProfileRepository;
    private final AccountRepository accountRepository;
    private final AllergenRepository allergenRepository;
    private final ProfileAllergenRepository profileAllergenRepository;
    private final AllergyProfileDTOConverter allergyProfileDTOConverter;
    private final AllergenDTOConverter allergenDTOConverter;


    public List<AllergyProfile> getAllAllergyProfile(){

        return allergyProfileRepository.findAll();
    }



    @Transactional
    public AllergyProfile createProfile(CreateAllergyProfileDTO createAllergyProfileDTO) {
        Account account = accountRepository.findById(UUID.fromString(createAllergyProfileDTO.getAccountId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        if (account.getAllergyProfile() != null) {
            throw new RuntimeException("Account already has an allergy profile");
        }

        AllergyProfile allergyProfile = new AllergyProfile();
        allergyProfile.setAccount(account);

        allergyProfileRepository.save(allergyProfile);
        account.setAllergyProfile(allergyProfile);
        accountRepository.save(account);

        for (AllergenIntensityDTO allergenDTO : createAllergyProfileDTO.getAllergens()) {
            Allergen allergen = allergenRepository.findById(UUID.fromString(allergenDTO.getAllergen_id()))
                    .orElseThrow(() -> new RuntimeException("Allergen not found"));

            ProfileAllergenId profileAllergenId = new ProfileAllergenId();
            profileAllergenId.setAllergenId(allergen.getAllergen_id());
            profileAllergenId.setProfileId(allergyProfile.getProfile_id());

            ProfileAllergen existingProfileAllergen = profileAllergenRepository.findById(profileAllergenId).orElse(null);

            if (existingProfileAllergen != null) {
                existingProfileAllergen.setIntensity(allergenDTO.getIntensity());
                profileAllergenRepository.save(existingProfileAllergen);
            } else {
                ProfileAllergen profileAllergen = new ProfileAllergen();
                profileAllergen.setId(profileAllergenId);
                profileAllergen.setAllergen(allergen);
                profileAllergen.setIntensity(allergenDTO.getIntensity());
                profileAllergen.setAllergyProfile(allergyProfile);

                profileAllergenRepository.save(profileAllergen);
            }
        }

        return allergyProfile;
    }


    @Transactional
    public AllergyProfile updateAllergyProfile(UUID profileId, UpdateAllergyProfileDTO updateAllergyProfileDTO) {
        AllergyProfile allergyProfile = allergyProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Allergy profile not found"));

        List<AllergenIntensityDTO> allergens = updateAllergyProfileDTO.getAllergens();
        if (allergens == null || allergens.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Allergens list cannot be null or empty");
        }

        Set<UUID> providedAllergenIds = allergens.stream()
                .map(dto -> UUID.fromString(dto.getAllergen_id()))
                .collect(Collectors.toSet());

        List<ProfileAllergen> existingProfileAllergens = profileAllergenRepository.findById_ProfileId(profileId);
        System.out.println("Current allergens in database: " + existingProfileAllergens);

        for (ProfileAllergen profileAllergen : existingProfileAllergens) {
            UUID allergenId = profileAllergen.getAllergen() != null ? profileAllergen.getAllergen().getAllergen_id() : null;

            System.out.println("Checking allergen from database: " + allergenId);

            if (allergenId != null && !providedAllergenIds.contains(allergenId)) {
                ProfileAllergenId profileAllergenId = new ProfileAllergenId(profileId, allergenId);

                Optional<ProfileAllergen> toDeleteOptional = profileAllergenRepository.findById(profileAllergenId);

                if (toDeleteOptional.isPresent()) {
                    ProfileAllergen toDelete = toDeleteOptional.get();
                    System.out.println("Attempting to delete: " + toDelete);
                    profileAllergenRepository.deleteProfileAllergen(profileId, allergenId);
                    System.out.println("Successfully deleted: " + toDelete);
                } else {
                    System.out.println("No ProfileAllergen found for deletion with ID: " + profileAllergenId);
                }
            } else {
                System.out.println("Allergen ID " + allergenId + " is still in the provided list, skipping deletion.");
            }
        }

        entityManager.flush();

        for (AllergenIntensityDTO allergenDTO : allergens) {
            String allergenIdStr = allergenDTO.getAllergen_id();
            if (allergenIdStr == null || allergenIdStr.trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Allergen ID cannot be null or empty");
            }

            UUID allergenId = UUID.fromString(allergenIdStr);
            Allergen allergen = allergenRepository.findById(allergenId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Allergen not found"));

            ProfileAllergenId profileAllergenId = new ProfileAllergenId(profileId, allergenId);
            ProfileAllergen profileAllergen = profileAllergenRepository.findById(profileAllergenId)
                    .orElseGet(() -> {
                        ProfileAllergen newProfileAllergen = new ProfileAllergen();
                        newProfileAllergen.setId(profileAllergenId);
                        newProfileAllergen.setAllergyProfile(allergyProfile);
                        newProfileAllergen.setAllergen(allergen);
                        return newProfileAllergen;
                    });

            profileAllergen.setIntensity(allergenDTO.getIntensity());
            profileAllergenRepository.save(profileAllergen);
        }

        entityManager.flush();

        allergyProfile.setProfileAllergens(profileAllergenRepository.findById_ProfileId(profileId));

        return allergyProfile;
    }








    public AllergyProfile getAllergyProfileById(UUID id) {


        //System.out.println("Profile allergens: " + profile.getAllergens());
        return allergyProfileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
    }


    public AllergyProfile getAllergyProfileByAccountId(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        // Check if the account has an associated allergy profile
        AllergyProfile allergyProfile = account.getAllergyProfile();
        if (allergyProfile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Allergy Profile not found for this account");
        }

        // Now we can safely get the profile_id
        UUID profile_id = allergyProfile.getProfile_id();
        return allergyProfileRepository.findById(profile_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Allergy profile not found"));
    }
}
