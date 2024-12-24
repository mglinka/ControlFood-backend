package com.project.mopa.service;

import com.project.mopa.entity.NutritionalValueGroup;
import com.project.mopa.entity.NutritionalValueName;
import com.project.mopa.repository.NutritionalValueGroupRepository;
import com.project.mopa.repository.NutritionalValueNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NutritionalValueService {
    private final NutritionalValueNameRepository nutritionalValueNameRepository;
    private final NutritionalValueGroupRepository nutritionalValueGroupRepository;

    public List<NutritionalValueName> getAllNutritionalValueNames() {
        return nutritionalValueNameRepository.findAll();
    }

    public List<NutritionalValueGroup> getAllNutritionalValueGroupNames() {
        return nutritionalValueGroupRepository.findAll();
    }
}
