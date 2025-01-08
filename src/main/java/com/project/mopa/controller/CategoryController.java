package com.project.mopa.controller;

import com.project.mopa.dto.converter.ProductDTOConverter;
import com.project.mopa.dto.product.GetCategoryDTO;
import com.project.mopa.service.CategoryService;
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
@RequestMapping("/api/v1/categories")
public class CategoryController {


    private final CategoryService categoryService;
    private final ProductDTOConverter productDTOConverter;

    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_USER')")
    @GetMapping
    public List<GetCategoryDTO> getAllCategories(){
        List<GetCategoryDTO> categoryDTOS = productDTOConverter.categoryDTOList(categoryService.getAllCategories());
        return ResponseEntity.status(HttpStatus.OK).body(categoryDTOS).getBody();
    }

}
