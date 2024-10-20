package com.project.mpa.service;

import com.project.exception.abstract_exception.AppException;
import com.project.exception.mok.AccountNotFoundException;
import com.project.mpa.dto.CreateAllergenDTO;
import com.project.mpa.entity.allergy.Allergen;
import com.project.mpa.repository.allergy.AllergenRepository;
import com.project.utils.messages.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void deleteAllergenById(UUID allergenId) throws AppException {
        if (!allergenRepository.existsById(allergenId)) {
            throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND);
        }

        allergenRepository.deleteById(allergenId);
    }
}
