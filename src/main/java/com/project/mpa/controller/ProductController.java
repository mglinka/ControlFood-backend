package com.project.mpa.controller;


import com.project.mpa.dto.converter.ProductDTOConverter;
import com.project.mpa.dto.product.CreateProductDTO;
import com.project.mpa.dto.product.GetProductDTO;
import com.project.mpa.entity.Product;
import com.project.mpa.service.ProductService;
import com.project.utils.ETagBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
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

    @PostMapping("/add")
    public ResponseEntity<GetProductDTO> createProduct(@Valid
                                                           @RequestBody CreateProductDTO createProductDTO) {
        Product createdProduct = productService.createProduct(createProductDTO);
        GetProductDTO getProductDTO = productDTOConverter.toProductDTO(createdProduct);
        return new ResponseEntity<>(getProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetProductDTO> getProductById(@PathVariable UUID id){
        GetProductDTO getProductDTO = productDTOConverter.toProductDTO(productService.getProductById(id));
        return ResponseEntity.status(HttpStatus.FOUND).body(getProductDTO);
    }

    @GetMapping("/by-ean/{ean}")
    public ResponseEntity<GetProductDTO> getProductByEan(@PathVariable String ean) {
        Product product = productService.findByEan(ean);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        String eTag = ETagBuilder.buildETag(product.getVersion().toString());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.ETAG,eTag)
                .body(productDTOConverter.toProductDTO(product));
    }

    @GetMapping("/withLabels")
    public List<GetProductDTO> getAllProductsWIthLabels(){
        List<GetProductDTO> getProductDTOS = productDTOConverter.productDTOList(productService.getAllProductsWithLabels());
        return ResponseEntity.status(HttpStatus.OK).body(getProductDTOS).getBody();
    }



}
