package com.project.mopa.service;

import com.project.mopa.entity.Unit;
import com.project.mopa.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;
    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }
}
