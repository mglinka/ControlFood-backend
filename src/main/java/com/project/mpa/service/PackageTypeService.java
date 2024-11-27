package com.project.mpa.service;

import com.project.mpa.entity.PackageType;
import com.project.mpa.repository.PackageTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageTypeService {

    private final PackageTypeRepository packageTypeRepository;

    public List<PackageType> getAllPackageTypes(){
        return packageTypeRepository.findAll();
    }
}
