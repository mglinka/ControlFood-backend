package com.project.mpa.dto.converter;

import com.project.mpa.dto.product.*;
import com.project.mpa.entity.*;
import com.project.mpa.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductDTOConverter {

    @Autowired
    private ModelMapper modelMapper;

    private final UnitRepository unitRepository;
    private final LabelRepository labelRepository;
    private final PackageTypeRepository packageTypeRepository;
    private final PortionRepository portionRepository;
    private final ProducerRepository producerRepository;
    private final ProductRepository productRepository;
    private final NutritionalIndexRepository nutritionalIndexRepository;
    private final CompositionRepository compositionRepository;

    public GetProductDTO toProductDTO(Product product) {
        return modelMapper.map(product, GetProductDTO.class);
    }

    public List<GetProductDTO> productDTOList(List<Product> products) {
        return products.stream().map(this::toProductDTO).collect(Collectors.toList());
    }

    @Transactional
    public Product toProduct(CreateProductDTO createProductDTO) {
        // Mapowanie danych DTO na encję Product
        Product product = modelMapper.map(createProductDTO, Product.class);
        Composition composition = toComposition(createProductDTO.getCompositionDTO());
        composition = compositionRepository.saveAndFlush(composition); // Zapisz kompozycję
        product.setComposition(composition);

        // Zapisz Label
        Label label = toLabel(createProductDTO.getLabelDTO());
        label = labelRepository.saveAndFlush(label); // Zapisz etykietę
        product.setLabel(label);

        // Zapisz Producenta
        Producer producer = toProducer(createProductDTO.getProducerDTO());
        producer = producerRepository.saveAndFlush(producer); // Zapisz producenta
        product.setProducer(producer); // Ustaw producenta w produkcie

        // Zapisz Typ Pakowania
        PackageType packageType = toPackageType(createProductDTO.getPackageTypeDTO());
        packageType = packageTypeRepository.saveAndFlush(packageType); // Zapisz typ pakowania
        product.setPackageType(packageType); // Ustaw typ pakowania w produkcie

        // Zapisz jednostkę używaną w produkcie (UnitDTO w Product)
        Unit productUnit = toUnit(createProductDTO.getUnitDTO());
        productUnit = unitRepository.saveAndFlush(productUnit); // Zapisz jednostkę dla produktu
        product.setUnit(productUnit); // Ustaw jednostkę w produkcie

        // Zapisz jednostkę dla porcji (UnitDTO w PortionDTO)
        Unit portionUnit = toUnit(createProductDTO.getPortionDTO().getUnitDTO());
        portionUnit = unitRepository.saveAndFlush(portionUnit); // Zapisz jednostkę dla porcji

        // Utwórz Portion i ustaw zapisaną jednostkę dla porcji
        Portion portion = toPortion(createProductDTO.getPortionDTO());
        portion.setUnit(portionUnit); // Ustaw zapisaną jednostkę na porcji
        portion = portionRepository.saveAndFlush(portion); // Zapisz porcję
        product.setPortion(portion); // Ustaw porcję w produkcie

        // Zapisz Nutritional Indexes
        Set<NutritionalIndex> nutritionalIndexes = createProductDTO.getNutritionalIndexDTOS().stream()
                .map(this::toNutritionalIndex)
                .map(nutritionalIndexRepository::saveAndFlush) // Zapisz każdy Nutritional Index osobno
                .collect(Collectors.toSet());
        product.setNutritionalIndexes(nutritionalIndexes);

        // Zapisz Nutritional Values i ich jednostki (UnitDTO w NutritionalValueDTO)
        List<NutritionalValue> nutritionalValues = createProductDTO.getNutritionalValueDTOS().stream()
                .map(dto -> {
                    // Tworzymy NutritionalValue
                    NutritionalValue nutritionalValue = toNutritionalValue(dto);

                    // Tworzymy i zapisujemy jednostkę dla NutritionalValue
                    Unit nutritionalUnit = toUnit(dto.getUnit());
                    nutritionalUnit = unitRepository.saveAndFlush(nutritionalUnit); // Zapisz jednostkę dla wartości odżywczej

                    // Ustawiamy zapisany unit na NutritionalValue
                    //nutritionalValue.setUnit(nutritionalUnit);

                    return nutritionalValue;
                })
                .collect(Collectors.toList());
        product.setNutritionalValues(nutritionalValues); // Ustaw wartości odżywcze w produkcie

        // Ustaw Product Indexes
        Set<ProductIndex> productIndexes = toProductIndexSet(createProductDTO.getProductIndexDTOS());
        product.setProductIndexes(productIndexes);

        // Ustaw Ratings
        Set<Rating> ratings = toRatingSet(createProductDTO.getRatingDTOS());
        product.setRatings(ratings);

        // Na koniec zapisz produkt
        return productRepository.saveAndFlush(product); // Zwróć zapisany produkt
    }




    private Producer toProducer(ProducerDTO producerDTO) {
        return modelMapper.map(producerDTO, Producer.class);
    }

    private PackageType toPackageType(PackageTypeDTO packageTypeDTO) {
        return modelMapper.map(packageTypeDTO, PackageType.class);
    }

    private Portion toPortion(PortionDTO portionDTO) {
        return modelMapper.map(portionDTO, Portion.class);
    }

    private Unit toUnit(UnitDTO unitDTO) {
        return modelMapper.map(unitDTO, Unit.class);
    }

    private NutritionalIndex toNutritionalIndex(NutritionalIndexDTO nutritionalIndexDTO) {
        return modelMapper.map(nutritionalIndexDTO, NutritionalIndex.class);
    }

    private Set<ProductIndex> toProductIndexSet(Set<ProductIndexDTO> productIndexDTOS) {
        return productIndexDTOS.stream()
                .map(dto -> modelMapper.map(dto, ProductIndex.class))
                .collect(Collectors.toSet());
    }

    private Label toLabel(LabelDTO labelDTO) {
        return modelMapper.map(labelDTO, Label.class);
    }

    private Set<Rating> toRatingSet(Set<RatingDTO> ratingDTOS) {
        return ratingDTOS.stream()
                .map(dto -> modelMapper.map(dto, Rating.class))
                .collect(Collectors.toSet());
    }

    private List<NutritionalValue> toNutritionalValueList(List<NutritionalValueDTO> nutritionalValueDTOS) {
        return nutritionalValueDTOS.stream()
                .map(dto -> modelMapper.map(dto, NutritionalValue.class))
                .collect(Collectors.toList());
    }
    private NutritionalValue toNutritionalValue(NutritionalValueDTO nutritionalValueDTO) {
        // Mapowanie DTO na encję NutritionalValue
        NutritionalValue nutritionalValue = modelMapper.map(nutritionalValueDTO, NutritionalValue.class);

        // Ustawienie jednostki (Unit) dla NutritionalValue
        Unit unit = toUnit(nutritionalValueDTO.getUnit());
        //nutritionalValue.setUnit(unit);

        return nutritionalValue;
    }
    private Composition toComposition(CompositionDTO compositionDTO) {
        return modelMapper.map(compositionDTO, Composition.class);
    }

}
