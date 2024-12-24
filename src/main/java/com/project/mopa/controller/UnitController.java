package com.project.mopa.controller;

import com.project.mopa.dto.converter.ProductDTOConverter;
import com.project.mopa.dto.product.UnitDTO;
import com.project.mopa.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/units")
public class UnitController {

    private final UnitService unitService;
    private final ProductDTOConverter productDTOConverter;

    @GetMapping
    public List<UnitDTO> getAllUnits() {
        List<UnitDTO> getUnitDTOS = productDTOConverter.unitDTOList(unitService.getAllUnits());
        return ResponseEntity.status(HttpStatus.OK).body(getUnitDTOS).getBody();
    }
}
