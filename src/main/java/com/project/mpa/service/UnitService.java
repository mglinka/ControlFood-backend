package com.project.mpa.service;

import com.project.mpa.entity.Unit;
import com.project.mpa.repository.UnitRepository;
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
