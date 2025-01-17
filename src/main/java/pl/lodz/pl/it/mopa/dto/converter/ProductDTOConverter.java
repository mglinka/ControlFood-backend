package pl.lodz.pl.it.mopa.dto.converter;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.pl.it.mopa.dto.product.*;
import pl.lodz.pl.it.mopa.entity.*;
import pl.lodz.pl.it.mopa.repository.*;

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
    private final NutritionalValueGroupRepository nutritionalValueGroupRepository;
    private final NutritionalValueNameRepository nutritionalValueNameRepository;
    private final CompositionRepository compositionRepository;
    private final FlavourRepository flavourRepository;
    private final ProductIndexRepository productIndexRepository;
    private final RatingRepository ratingRepository;
    private final CategoryRepository categoryRepository;

    public GetProductDTO toProductDTO(Product product) {
        GetProductDTO dto = modelMapper.map(product, GetProductDTO.class);

        GetCategoryDTO getCategoryDTO = new GetCategoryDTO();
        getCategoryDTO.setName(product.getCategory().getName());
        getCategoryDTO.setId(product.getCategory().getId());

        dto.setCategoryDTO(getCategoryDTO);

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

        if (product.getComposition() != null) {
            Composition composition = product.getComposition();
            CompositionDTO compositionDTO = new CompositionDTO();

            if (composition.getIngredients() != null) {
                List<IngredientDTO> ingredientDTOS = composition.getIngredients().stream()
                        .map(ingredient -> new IngredientDTO(ingredient.getName()))
                        .collect(Collectors.toList());
                compositionDTO.setIngredientDTOS(ingredientDTOS);
            }

            if (composition.getAdditions() != null) {
                List<AdditionDTO> additionDTOS = composition.getAdditions().stream()
                        .map(addition -> new AdditionDTO(addition.getName()))
                        .collect(Collectors.toList());
                compositionDTO.setAdditionDTOS(additionDTOS);
            }

            if (composition.getFlavour() != null) {
                FlavourDTO flavourDTO = new FlavourDTO();
                flavourDTO.setName(composition.getFlavour().getName());
                compositionDTO.setFlavourDTO(flavourDTO);
            }

            dto.setCompositionDTO(compositionDTO);
        }

        if (product.getNutritionalValues() != null) {
            List<NutritionalValueDTO> nutritionalValueDTOS = product.getNutritionalValues().stream()
                    .map(nutritionalValue -> {
                        NutritionalValueDTO nutritionalValueDTO = new NutritionalValueDTO();

                        NutritionalValueNameDTO nameDTO = new NutritionalValueNameDTO();
                        nameDTO.setName(nutritionalValue.getNutritionalValueName().getName());

                        if (nutritionalValue.getNutritionalValueName().getGroup() != null) {
                            NutritionalValueGroupDTO groupDTO = new NutritionalValueGroupDTO();
                            groupDTO.setGroupName(nutritionalValue.getNutritionalValueName().getGroup().getGroupName());
                            nameDTO.setGroup(groupDTO);
                        }
                        nutritionalValueDTO.setNutritionalValueName(nameDTO);

                        nutritionalValueDTO.setQuantity(nutritionalValue.getQuantity());

                        if (nutritionalValue.getUnit() != null) {
                            UnitDTO unitDTO = new UnitDTO();
                            unitDTO.setName(nutritionalValue.getUnit().getName());
                            nutritionalValueDTO.setUnit(unitDTO);
                        }

                        nutritionalValueDTO.setNrv(nutritionalValue.getNrv());

                        return nutritionalValueDTO;
                    })
                    .collect(Collectors.toList());

            dto.setNutritionalValueDTOS(nutritionalValueDTOS);
        }

        return dto;
    }

    public GetCategoryDTO toCategoryDTO (Category category){
        return modelMapper.map(category, GetCategoryDTO.class);
    }

    public UnitDTO toUnitDTO(Unit unit){
        return modelMapper.map(unit, UnitDTO.class);
    }

    public PackageTypeDTO toPackageTypeDTO(PackageType packageType){
        return modelMapper.map(packageType, PackageTypeDTO.class);
    }

    public NutritionalValueNameDTO toNutritionalValueNameDTO(NutritionalValueName nutritionalValueName){
        return modelMapper.map(nutritionalValueName, NutritionalValueNameDTO.class);
    }
    public NutritionalValueGroupDTO toNutritionalValueGroupDTO(NutritionalValueGroup nutritionalValueGroup){
        return modelMapper.map(nutritionalValueGroup, NutritionalValueGroupDTO.class);
    }
    public List<GetProductDTO> productDTOList(List<Product> products) {
        return products.stream().map(this::toProductDTO).collect(Collectors.toList());
    }
    public List<UnitDTO> unitDTOList(List<Unit> units) {
        return units.stream().map(this::toUnitDTO).collect(Collectors.toList());
    }

    public List<PackageTypeDTO> packageTypeDTOList(List<PackageType> packageTypes){
        return packageTypes.stream().map(this::toPackageTypeDTO).collect(Collectors.toList());
    }

    public List<NutritionalValueNameDTO> nutritionalValueNameDTOList(List<NutritionalValueName> nutritionalValueNames) {
        return nutritionalValueNames.stream().map(this::toNutritionalValueNameDTO).collect(
                Collectors.toList()
        );
    }
    public List<NutritionalValueGroupDTO> nutritionalValueGroupDTOList(List<NutritionalValueGroup> nutritionalValueGroups){
        return nutritionalValueGroups.stream().map(this::toNutritionalValueGroupDTO).collect(Collectors.toList());
    }

    public List<GetCategoryDTO> categoryDTOList (List<Category> categories){
        return categories.stream().map(this::toCategoryDTO).collect(Collectors.toList());
    }


    @Transactional
    public Product toProduct(CreateProductDTO createProductDTO) {
        Product product = modelMapper.map(createProductDTO, Product.class);
        product.setComposition(saveAndSetComposition(createProductDTO.getCompositionDTO()));
        product.setLabel(saveAndSetLabel(createProductDTO.getLabelDTO()));
        product.setProducer(saveAndSetProducer(createProductDTO.getProducerDTO()));
        product.setPackageType(findPackageType(createProductDTO.getPackageTypeDTO()));

        product.setCategory(findCategory(createProductDTO.getCategory()));

        product.setUnit(findUnit(createProductDTO.getUnitDTO()));
        product.setPortion(saveAndSetPortion(createProductDTO.getPortionDTO()));

        product.setNutritionalValues(saveAndSetNutritionalValues(createProductDTO.getNutritionalValueDTOS()));

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

    private PackageType findPackageType(PackageTypeDTO packageTypeDTO) {
        return packageTypeRepository.findByName(packageTypeDTO.getName()).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PackageType is not found")
        );
    }

    private Flavour saveAndSetFlavour(FlavourDTO flavourDTO) {
        Flavour flavour = modelMapper.map(flavourDTO, Flavour.class);
        return flavourRepository.saveAndFlush(flavour);
    }

    private Unit findUnit(UnitDTO unitDTO) {
        return unitRepository.findByName(unitDTO.getName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unit is missing")
        );
    }

    private Category findCategory(GetCategoryDTO categoryDTO) {
        return categoryRepository.getCategoryByName(categoryDTO.getName())
                .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category is missing")
        );
    }
    private NutritionalValueGroup findNutritionalValueGroup (NutritionalValueGroupDTO nutritionalValueGroupDTO) {
        return nutritionalValueGroupRepository.findByGroupName(nutritionalValueGroupDTO.getGroupName()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group name not found")
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
        NutritionalValueGroup nutritionalValueGroup = findNutritionalValueGroup(dto.getNutritionalValueName().getGroup());
        nutritionalValueName.setGroup(nutritionalValueGroup);
        return nutritionalValueRepository.saveAndFlush(nutritionalValue);
    }

    private NutritionalValueName findNutritionalValueName(NutritionalValueNameDTO nutritionalValueName) {
        return nutritionalValueNameRepository.findByName(nutritionalValueName.getName()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutritiion value name is missing."));
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