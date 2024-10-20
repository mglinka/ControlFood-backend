package com.project.mpa.controller;

import com.project.mpa.dto.GetAllergenDTO;
import com.project.mpa.dto.converter.ProductDTOConverter;
import com.project.mpa.dto.product.CreateProductDTO;
import com.project.mpa.dto.product.GetProductDTO;
import com.project.mpa.entity.Product;
import com.project.mpa.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductDTOConverter productDTOConverter;


    @GetMapping
    public List<GetProductDTO> getAllProducts() {
        List<GetProductDTO> getProductDTOS = productDTOConverter.productDTOList(productService.getAllProducts());
        return ResponseEntity.status(HttpStatus.OK).body(getProductDTOS).getBody();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductDTO createProductDTO) {
        Product createdProduct = productService.createProduct(createProductDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

}
