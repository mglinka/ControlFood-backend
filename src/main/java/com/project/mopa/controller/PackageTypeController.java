package com.project.mopa.controller;


import com.project.mopa.dto.converter.ProductDTOConverter;
import com.project.mopa.dto.product.PackageTypeDTO;
import com.project.mopa.service.PackageTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_USER')")
    @GetMapping
    public List<PackageTypeDTO> getAllPackageTypes() {
        List<PackageTypeDTO> packageTypeDTOS = productDTOConverter.packageTypeDTOList(packageTypeService.getAllPackageTypes());
        return ResponseEntity.status(HttpStatus.OK).body(packageTypeDTOS).getBody();
    }

}
