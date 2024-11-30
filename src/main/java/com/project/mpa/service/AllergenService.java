package com.project.mpa.service;

import com.project.mpa.dto.CreateAllergenDTO;
import com.project.mpa.dto.product.UpdateAllergenDTO;
import com.project.mpa.entity.allergy.Allergen;
import com.project.mpa.repository.allergy.AllergenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        System.out.println("Service");
        Allergen allergen = new Allergen();
        allergen.setName(createAllergenDTO.getName());

        allergen.setType(createAllergenDTO.getType());

        System.out.println("TU" + allergen.getType().name());
        System.out.println("TUbb" + allergen.getType());

        return allergenRepository.save(allergen);
    }



    public void deleteAllergenById(UUID allergenId) {
        if (!allergenRepository.existsById(allergenId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        allergenRepository.deleteById(allergenId);
    }

    @Transactional
    public Allergen editAllergen(UUID id, UpdateAllergenDTO updateAllergenDTO) {
        Allergen allergen = allergenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Allergen not found"));

        allergen.setName(updateAllergenDTO.getName());
        allergen.setType(updateAllergenDTO.getType());
        allergenRepository.save(allergen);
        return allergen;
    }

}
