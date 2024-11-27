package com.project.mpa.controller;


import com.project.mpa.dto.converter.ProductDTOConverter;
import com.project.mpa.dto.product.PackageTypeDTO;
import com.project.mpa.service.PackageTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/package-types")
public class PackageTypeController {

    private final ProductDTOConverter productDTOConverter;
    private final PackageTypeService packageTypeService;

    @GetMapping
    public List<PackageTypeDTO> getAllPackageTypes() {
        List<PackageTypeDTO> packageTypeDTOS = productDTOConverter.packageTypeDTOList(packageTypeService.getAllPackageTypes());
        return ResponseEntity.status(HttpStatus.OK).body(packageTypeDTOS).getBody();
    }

}
