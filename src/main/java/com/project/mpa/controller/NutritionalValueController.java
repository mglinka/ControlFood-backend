package com.project.mpa.controller;

import com.project.mpa.dto.converter.ProductDTOConverter;
import com.project.mpa.dto.product.NutritionalValueGroupDTO;
import com.project.mpa.dto.product.NutritionalValueNameDTO;
import com.project.mpa.dto.product.UnitDTO;
import com.project.mpa.service.NutritionalValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/nutritional-value")
public class NutritionalValueController {

    private final NutritionalValueService nutritionalValueService;
    private final ProductDTOConverter productDTOConverter;

    @GetMapping("/names")
    public List<NutritionalValueNameDTO> getAllNutritionalValueNames() {
        List<NutritionalValueNameDTO> getNutritionalValueNames = productDTOConverter.nutritionalValueNameDTOList(nutritionalValueService.getAllNutritionalValueNames());
        return ResponseEntity.status(HttpStatus.OK).body(getNutritionalValueNames).getBody();
    }

    @GetMapping("/group-names")
    public List<NutritionalValueGroupDTO> getAllNutritionalValueGroupNames() {
        List<NutritionalValueGroupDTO> getNutritionalValueGroupNames = productDTOConverter.nutritionalValueGroupDTOList(nutritionalValueService.getAllNutritionalValueGroupNames());
        return ResponseEntity.status(HttpStatus.OK).body(getNutritionalValueGroupNames).getBody();
    }
}
