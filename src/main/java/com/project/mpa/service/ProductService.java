package com.project.mpa.service;

import com.project.exception.abstract_exception.NotFoundException;
import com.project.mpa.dto.converter.ProductDTOConverter;
import com.project.mpa.dto.product.CreateProductDTO;
import com.project.mpa.entity.*;
import com.project.mpa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final LabelRepository labelRepository;
    private final ProductDTOConverter productDTOConverter;

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Product createProduct(CreateProductDTO createProductDTO) {
        return productDTOConverter.toProduct(createProductDTO);
    }
    public Product findByEan(String ean) {
        return productRepository.findByEan(ean);
    }


    public List<Product> getAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);  // First page, 10 items
        return productRepository.findAll(pageable).getContent();  // Fetch the 10 items
    }


    public List<Product> getAllProductsWithLabels(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAllProductsWithLabels(pageable);
    }

}
