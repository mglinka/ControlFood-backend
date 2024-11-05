package com.project.mpa.service;

import com.project.mpa.entity.NutritionalValueGroup;
import com.project.mpa.entity.NutritionalValueName;
import com.project.mpa.entity.Unit;
import com.project.mpa.repository.NutritionalValueGroupRepository;
import com.project.mpa.repository.NutritionalValueNameRepository;
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
