package com.project.mpa.dto.converter;

import com.project.exception.abstract_exception.BadRequestException;
import com.project.mpa.dto.product.*;
import com.project.mpa.entity.*;
import com.project.mpa.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductDTOConverter {

    private final ModelMapper modelMapper;
    private final UnitRepository unitRepository;
    private final LabelRepository labelRepository;
    private final PackageTypeRepository packageTypeRepository;
    private final PortionRepository portionRepository;
    private final ProducerRepository producerRepository;
    private final ProductRepository productRepository;
    private final NutritionalIndexRepository nutritionalIndexRepository;
    private final NutritionalValueRepository nutritionalValueRepository;
    private final NutritionalValueNameRepository nutritionalValueNameRepository;
    private final CompositionRepository compositionRepository;
    private final FlavourRepository flavourRepository;
    private final ProductIndexRepository productIndexRepository;
    private final RatingRepository ratingRepository;

    public GetProductDTO toProductDTO(Product product) {
        GetProductDTO dto = modelMapper.map(product, GetProductDTO.class);

        if (product.getLabel() != null) {
            Label label = product.getLabel();
            LabelDTO labelDTO = new LabelDTO();

            labelDTO.setStorage(label.getStorage());
            labelDTO.setDurability(label.getDurability());
            labelDTO.setInstructionsAfterOpening(label.getInstructionsAfterOpening());
            labelDTO.setPreparation(label.getPreparation());
            labelDTO.setAllergens(label.getAllergens());


            if (label.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(label.getImage());
                labelDTO.setImage(base64Image);
            }

            dto.setLabelDTO(labelDTO);
        }

        return dto;
    }

    public UnitDTO toUnitDTO(Unit unit){
        return modelMapper.map(unit, UnitDTO.class);
    }

    public List<GetProductDTO> productDTOList(List<Product> products) {
        return products.stream().map(this::toProductDTO).collect(Collectors.toList());
    }
    public List<UnitDTO> unitDTOList(List<Unit> units) {
        return units.stream().map(this::toUnitDTO).collect(Collectors.toList());
    }

    @Transactional
    public Product toProduct(CreateProductDTO createProductDTO) {
        Product product = modelMapper.map(createProductDTO, Product.class);
        product.setComposition(saveAndSetComposition(createProductDTO.getCompositionDTO()));
        product.setLabel(saveAndSetLabel(createProductDTO.getLabelDTO()));
        product.setProducer(saveAndSetProducer(createProductDTO.getProducerDTO()));
        product.setPackageType(saveAndSetPackageType(createProductDTO.getPackageTypeDTO()));

        product.setUnit(findUnit(createProductDTO.getUnitDTO()));
        product.setPortion(saveAndSetPortion(createProductDTO.getPortionDTO()));
        product.setNutritionalIndexes(saveAndSetNutritionalIndexes(createProductDTO.getNutritionalIndexDTOS()));
        // TU
        product.setNutritionalValues(saveAndSetNutritionalValues(createProductDTO.getNutritionalValueDTOS()));
        product.setProductIndexes(toProductIndexSet(createProductDTO.getProductIndexDTOS()));
        product.setRatings(toRatingSet(createProductDTO.getRatingDTOS()));

        return productRepository.saveAndFlush(product);
    }

    private Composition saveAndSetComposition(CompositionDTO compositionDTO) {
        Composition composition = modelMapper.map(compositionDTO, Composition.class);
        composition.setFlavour(saveAndSetFlavour(compositionDTO.getFlavourDTO()));
        return compositionRepository.saveAndFlush(composition);
    }

    private Label saveAndSetLabel(LabelDTO labelDTO) {
        Label label = modelMapper.map(labelDTO, Label.class);
        return labelRepository.saveAndFlush(label);
    }

    private Producer saveAndSetProducer(ProducerDTO producerDTO) {
        Producer producer = modelMapper.map(producerDTO, Producer.class);
        return producerRepository.saveAndFlush(producer);
    }

    private PackageType saveAndSetPackageType(PackageTypeDTO packageTypeDTO) {
        PackageType packageType = modelMapper.map(packageTypeDTO, PackageType.class);
        return packageTypeRepository.saveAndFlush(packageType);
    }

    private Flavour saveAndSetFlavour(FlavourDTO flavourDTO) {
        Flavour flavour = modelMapper.map(flavourDTO, Flavour.class);
        return flavourRepository.saveAndFlush(flavour);
    }

    private Unit findUnit(UnitDTO unitDTO) {
        return unitRepository.findByName(unitDTO.getName()).orElseThrow(
                () -> new BadRequestException("Unit is missing")
        );
    }

    private Portion saveAndSetPortion(PortionDTO portionDTO) {
        Portion portion = modelMapper.map(portionDTO, Portion.class);
        Unit portionUnit = findUnit(portionDTO.getUnitDTO());
        portion.setUnit(portionUnit);
        return portionRepository.saveAndFlush(portion);
    }

    private Set<NutritionalIndex> saveAndSetNutritionalIndexes(Set<NutritionalIndexDTO> nutritionalIndexDTOS) {
        return nutritionalIndexDTOS.stream()
                .map(dto -> modelMapper.map(dto, NutritionalIndex.class))
                .map(nutritionalIndexRepository::saveAndFlush)
                .collect(Collectors.toSet());

    }

    private List<NutritionalValue> saveAndSetNutritionalValues(List<NutritionalValueDTO> nutritionalValueDTOS) {
        return nutritionalValueDTOS.stream()
                .map(this::saveAndSetNutritionalValue)
                .collect(Collectors.toList());

    }

    private NutritionalValue saveAndSetNutritionalValue(NutritionalValueDTO dto) {
        NutritionalValue nutritionalValue = modelMapper.map(dto, NutritionalValue.class);
        Unit unit = findUnit(dto.getUnit());
        NutritionalValueName nutritionalValueName = findNutritionalValueName(dto.getNutritionalValueName());
        nutritionalValue.setUnit(unit);
        nutritionalValue.setNutritionalValueName(nutritionalValueName);

        return nutritionalValueRepository.saveAndFlush(nutritionalValue);
    }

    private NutritionalValueName findNutritionalValueName(NutritionalValueNameDTO nutritionalValueName) {
        return nutritionalValueNameRepository.findByName(nutritionalValueName.getName()).orElseThrow(()-> new BadRequestException("Nutritiion value name is missing."));
    }

    private Set<ProductIndex> toProductIndexSet(Set<ProductIndexDTO> productIndexDTOS) {
        return productIndexDTOS.stream()
                .map(dto -> modelMapper.map(dto, ProductIndex.class))
                .map(this.productIndexRepository::saveAndFlush)
                .collect(Collectors.toSet());
    }

    private Set<Rating> toRatingSet(Set<RatingDTO> ratingDTOS) {
        return ratingDTOS.stream()
                .map(dto -> modelMapper.map(dto, Rating.class))
                .map(this.ratingRepository::saveAndFlush)
                .collect(Collectors.toSet());
    }

}