package com.project.mopa.controller;


import com.project.mopa.dto.converter.ProductDTOConverter;
import com.project.mopa.dto.product.CreateProductDTO;
import com.project.mopa.dto.product.GetProductDTO;
import com.project.mopa.entity.Product;
import com.project.mopa.service.ProductService;
import com.project.utils.ETagBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_USER')")
    @GetMapping
    public List<GetProductDTO> getAllProducts() {
        List<GetProductDTO> getProductDTOS = productDTOConverter.productDTOList(productService.getAllProducts());
        return ResponseEntity.status(HttpStatus.OK).body(getProductDTOS).getBody();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/add")
    public ResponseEntity<GetProductDTO> createProduct(@Valid
                                                           @RequestBody CreateProductDTO createProductDTO) {
        Product createdProduct = productService.createProduct(createProductDTO);
        GetProductDTO getProductDTO = productDTOConverter.toProductDTO(createdProduct);
        return new ResponseEntity<>(getProductDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<GetProductDTO> getProductById(@PathVariable UUID id){
        GetProductDTO getProductDTO = productDTOConverter.toProductDTO(productService.getProductById(id));
        return ResponseEntity.status(HttpStatus.FOUND).body(getProductDTO);
    }


    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_USER')")
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

    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_USER')")
    @GetMapping("/withLabels")
    public List<GetProductDTO> getAllProductsWIthLabels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "") String query) {

        List<GetProductDTO> getProductDTOS = productDTOConverter.productDTOList(productService.getAllProductsWithLabels(page,size, query));
        return ResponseEntity.status(HttpStatus.OK).body(getProductDTOS).getBody();
    }

    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_USER')")
    @GetMapping("/category")
    public ResponseEntity<List<GetProductDTO>> getProductsByCategory(@RequestParam String categoryName) {
        List<GetProductDTO> productDTOList = productDTOConverter.productDTOList(productService.getAllProductsByCategoryName(categoryName));
        return ResponseEntity.ok(productDTOList);
    }

    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_USER')")
    @GetMapping("/withoutPagination")
    public List<GetProductDTO> getAllProductsWithoutPagination() {
        List<GetProductDTO> getProductDTOS = productDTOConverter.productDTOList(productService.getAllProductsWithoutPagination());
        return ResponseEntity.status(HttpStatus.OK).body(getProductDTOS).getBody();
    }








}
