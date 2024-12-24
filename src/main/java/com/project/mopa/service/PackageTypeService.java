package com.project.mopa.service;

import com.project.mopa.entity.PackageType;
import com.project.mopa.repository.PackageTypeRepository;
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
