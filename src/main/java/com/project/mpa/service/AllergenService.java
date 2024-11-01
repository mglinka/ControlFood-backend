package com.project.mpa.service;

import com.project.mpa.dto.CreateAllergenDTO;
import com.project.mpa.entity.allergy.Allergen;
import com.project.mpa.repository.allergy.AllergenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AllergenService {

    private final AllergenRepository allergenRepository;


    public List<Allergen> getAllAllergens() {
        return allergenRepository.findAll();

    }

    public Allergen createAllergen(CreateAllergenDTO createAllergenDTO) {
        Allergen allergen = new Allergen();
        allergen.setName(createAllergenDTO.getName());

        return allergenRepository.save(allergen);
    }

    public void deleteAllergenById(UUID allergenId) {
        if (!allergenRepository.existsById(allergenId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        allergenRepository.deleteById(allergenId);
    }
}
