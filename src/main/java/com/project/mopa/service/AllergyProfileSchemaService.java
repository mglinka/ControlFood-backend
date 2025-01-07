package com.project.mopa.service;

import com.project.entity.allergy.Allergen;
import com.project.entity.allergy.AllergyProfileSchema;
import com.project.mopa.dto.AllergenDTO;
import com.project.mopa.dto.CreateAllergyProfileSchemaDTO;
import com.project.mopa.dto.UpdateAllergyProfileSchemaDTO;
import com.project.mok.repository.allergy.AllergenRepository;
import com.project.mok.repository.allergy.AllergyProfileSchemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AllergyProfileSchemaService {

    private final AllergyProfileSchemaRepository allergyProfileSchemaRepository;
    private final AllergenRepository allergenRepository;


    public List<AllergyProfileSchema> getAllAllergyProfile(){

        return allergyProfileSchemaRepository.findAll();
    }

    public AllergyProfileSchema getAllergyProfileSchemaById(UUID id) {
        return allergyProfileSchemaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
    }

    public AllergyProfileSchema createProfile(CreateAllergyProfileSchemaDTO createAllergyProfileSchemaDTO) {
        Optional<AllergyProfileSchema> existingProfile = allergyProfileSchemaRepository.findByName(createAllergyProfileSchemaDTO.getName());
        if (existingProfile.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile with this name already exists");
        }

        AllergyProfileSchema allergyProfileSchema = new AllergyProfileSchema();
        allergyProfileSchema.setName(createAllergyProfileSchemaDTO.getName());

        List<Allergen> allergens = new ArrayList<>();
        for (AllergenDTO allergenDTO : createAllergyProfileSchemaDTO.getAllergens()) {
            Allergen allergen = allergenRepository.findById(allergenDTO.getAllergen_id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Allergen not found"));
            allergens.add(allergen);
        }

        allergyProfileSchema.setAllergens(allergens);

        return allergyProfileSchemaRepository.save(allergyProfileSchema);
    }


    @Transactional
    public void editProfile(UpdateAllergyProfileSchemaDTO updateAllergyProfileSchemaDTO) {
        AllergyProfileSchema allergyProfileSchema = allergyProfileSchemaRepository.findById(updateAllergyProfileSchemaDTO.getSchema_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "AllergyProfileSchema not found"));

        if (updateAllergyProfileSchemaDTO.getName() != null && !updateAllergyProfileSchemaDTO.getName().isEmpty()) {
            Optional<AllergyProfileSchema> existingProfile = allergyProfileSchemaRepository.findByName(updateAllergyProfileSchemaDTO.getName());
            if (existingProfile.isPresent() && !existingProfile.get().getSchema_id().equals(allergyProfileSchema.getSchema_id())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile with this name already exists");
            }
            allergyProfileSchema.setName(updateAllergyProfileSchemaDTO.getName());
        }

        List<Allergen> allergens = new ArrayList<>();
        Set<UUID> newAllergenIds = new HashSet<>();

        for (AllergenDTO allergenDTO : updateAllergyProfileSchemaDTO.getAllergens()) {
            UUID allergenId = allergenDTO.getAllergen_id();

            if (newAllergenIds.contains(allergenId)) {
                continue;
            }
            newAllergenIds.add(allergenId);

            Allergen allergen = allergenRepository.findById(allergenId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Allergen not found"));

            allergens.add(allergen);
        }

        allergyProfileSchema.setAllergens(allergens);
        allergyProfileSchemaRepository.save(allergyProfileSchema);
    }


    @Transactional
    public void deleteAllergyProfileSchema(UUID id) {
        AllergyProfileSchema allergyProfileSchema = allergyProfileSchemaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        allergyProfileSchemaRepository.delete(allergyProfileSchema);
    }



}
