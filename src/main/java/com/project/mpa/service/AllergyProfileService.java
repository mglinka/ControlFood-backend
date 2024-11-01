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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
            Allergen allergen = allergenRepository.findById(UUID.fromString(allergenDTO.getAllergenId()))
                    .orElseThrow(() -> new RuntimeException("Allergen not found"));

            ProfileAllergenId profileAllergenId = new ProfileAllergenId();
            profileAllergenId.setAllergen_id(allergen.getAllergen_id());
            profileAllergenId.setProfile_id(allergyProfile.getProfile_id());

            ProfileAllergen existingProfileAllergen = profileAllergenRepository.findById(profileAllergenId).orElse(null);

            if (existingProfileAllergen != null) {
                existingProfileAllergen.setIntensity(allergenDTO.getIntensity());
                profileAllergenRepository.save(existingProfileAllergen);
//                savedProfile.getProfileAllergens().add(existingProfileAllergen);
            } else {
                ProfileAllergen profileAllergen = new ProfileAllergen();
                profileAllergen.setId(profileAllergenId);
                profileAllergen.setAllergen(allergen);
                profileAllergen.setIntensity(allergenDTO.getIntensity());
                profileAllergen.setAllergyProfile(allergyProfile);

                profileAllergenRepository.save(profileAllergen);
//                allergyProfile.getProfileAllergens().add(profileAllergen);
            }
            System.out.println("mam dosc" + allergenDTO.toString());
        }

        return allergyProfile;
    }






    public AllergyProfile updateAllergyProfile(UUID id, UpdateAllergyProfileDTO updateAllergyProfileDTO) {

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
