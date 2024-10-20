package com.project.mpa.service;

import com.project.mpa.dto.converter.ProductDTOConverter;
import com.project.mpa.dto.product.CreateProductDTO;
import com.project.mpa.entity.*;
import com.project.mpa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProducerRepository producerRepository;
    private final LabelRepository labelRepository;
    private final IngredientRepository ingredientRepository;
    private final FlavourRepository flavourRepository;
    private final AdditionRepository additionRepository;
    private final ProductIndexRepository productIndexRepository;
    private final NutritionalIndexRepository nutritionalIndexRepository;
    private final NutritionalValueRepository nutritionalValueRepository;
    private final NutritionalValueNameRepository nutritionalValueNameRepository;
    private final NutritionalValueGroupRepository nutritionalValueGroupRepository;
    private final PackageTypeRepository packageTypeRepository;
    private final PortionRepository portionRepository;
    private final RatingRepository ratingRepository;
    private final UnitRepository unitRepository;
    private final CompositionRepository compositionRepository;
    private final ProductDTOConverter productDTOConverter;


    @Transactional
    public Product createProduct(CreateProductDTO createProductDTO) {
        Product product = productDTOConverter.toProduct(createProductDTO);

        Composition composition = product.getComposition();

        compositionRepository.saveAndFlush(composition); // Save composition
        // If Flavour is part of composition, make sure to save it as well
        if (composition.getFlavour() != null) {
            flavourRepository.saveAndFlush(composition.getFlavour());
        }

        // Now save the Product entity
        return productRepository.saveAndFlush(product);
    }


    public List<Product> getAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);  // First page, 10 items
        return productRepository.findAll(pageable).getContent();  // Fetch the 10 items
    }
}
