package com.project.mopa.service;

import com.project.mopa.dto.converter.ProductDTOConverter;
import com.project.mopa.dto.product.CreateProductDTO;
import com.project.mopa.entity.*;
import com.project.mopa.repository.*;
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
    private final CategoryRepository categoryRepository;
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


    public List<Product> getAllProductsWithLabels(int page, int size, String query) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAllProductsWithLabels(pageable, query);

    }

    public List<Product> getAllProductsByCategoryName(String categoryName){
        Category category = categoryRepository.getCategoryByName(categoryName).orElseThrow();
        return productRepository.findByCategoryId(category.getId());
    }

    public List<Product> getAllProductsWithoutPagination() {
        return productRepository.findAll();
    }
}
