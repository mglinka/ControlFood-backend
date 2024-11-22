package com.project.mpa.service;

import com.project.mpa.dto.AllergenDTO;
import com.project.mpa.dto.CreateAllergyProfileSchemaDTO;
import com.project.mpa.dto.UpdateAllergyProfileSchemaDTO;
import com.project.mpa.entity.allergy.*;
import com.project.mpa.repository.allergy.AllergenRepository;
import com.project.mpa.repository.allergy.AllergyProfileSchemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

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
