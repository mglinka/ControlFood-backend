package com.project.mpa.service;


import com.project.entity.Account;
import com.project.exception.abstract_exception.AppException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AllergyProfileService {

    private final AllergyProfileRepository allergyProfileRepository;
    private final AccountRepository accountRepository;
    private final AllergenRepository allergenRepository;
    private final ProfileAllergenRepository profileAllergenRepository;
    private final AllergyProfileDTOConverter allergyProfileDTOConverter;
    private final AllergenDTOConverter allergenDTOConverter;

    public List<AllergyProfile> getAllAllergyProfile(){

        return allergyProfileRepository.findAll();
    }



    public AllergyProfile createProfile(CreateAllergyProfileDTO createAllergyProfileDTO) {
        // Fetch the associated Account
        Account account = accountRepository.findById(UUID.fromString(createAllergyProfileDTO.getAccountId()))
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Create a new AllergyProfile instance
        AllergyProfile allergyProfile = new AllergyProfile();
        allergyProfile.setAccount(account);

        // Save the AllergyProfile to generate the profile_id
        AllergyProfile savedProfile = allergyProfileRepository.save(allergyProfile);

        // Loop through the allergens from the DTO
        for (AllergenIntensityDTO allergenDTO : createAllergyProfileDTO.getAllergens()) {
            // Fetch the associated Allergen
            Allergen allergen = allergenRepository.findById(UUID.fromString(allergenDTO.getAllergenId()))
                    .orElseThrow(() -> new RuntimeException("Allergen not found"));

            // Create a new ProfileAllergen instance
            ProfileAllergen profileAllergen = new ProfileAllergen();
            ProfileAllergenId profileAllergenId = new ProfileAllergenId();

            // Set the allergen_id and the profile_id (now safe to use savedProfile's ID)
            profileAllergenId.setAllergen_id(allergen.getAllergen_id());
            profileAllergenId.setProfile_id(savedProfile.getProfile_id()); // Use the ID of the saved profile

            // Set the ID and other properties
            profileAllergen.setId(profileAllergenId);
            profileAllergen.setAllergen(allergen);
            profileAllergen.setIntensity(allergenDTO.getIntensity());

            // Set the relationship back to the AllergyProfile
            profileAllergen.setAllergyProfile(savedProfile);

            // Add the ProfileAllergen to the AllergyProfile
            savedProfile.getProfileAllergens().add(profileAllergen);
        }

        // Save the updated AllergyProfile with its allergens
        return allergyProfileRepository.save(savedProfile);
    }





    public AllergyProfile updateAllergyProfile(UUID id, UpdateAllergyProfileDTO updateAllergyProfileDTO) throws AppException {

        return null;
    }

    @Transactional(readOnly = true)
    public AllergyProfile getAllergyProfileById(UUID id) {

        AllergyProfile profile = allergyProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));


        //System.out.println("Profile allergens: " + profile.getAllergens());
        return profile;
    }
}
