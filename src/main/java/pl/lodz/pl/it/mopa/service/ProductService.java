package pl.lodz.pl.it.mopa.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.pl.it.mopa.dto.converter.ProductDTOConverter;
import pl.lodz.pl.it.mopa.dto.product.CreateProductDTO;
import pl.lodz.pl.it.mopa.entity.Category;
import pl.lodz.pl.it.mopa.entity.Product;
import pl.lodz.pl.it.mopa.repository.CategoryRepository;
import pl.lodz.pl.it.mopa.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductDTOConverter productDTOConverter;

  public Product getProductById(UUID id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
  }

  @Transactional
  public Product createProduct(CreateProductDTO createProductDTO) {
    return productDTOConverter.toProduct(createProductDTO);
  }

  public Product findByEan(String ean) {
    return productRepository.findByEan(ean).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
  }


  public List<Product> getAllProducts() {
    Pageable pageable = PageRequest.of(0, 10);  // First page, 10 items
    return productRepository.findAll(pageable).getContent();  // Fetch the 10 items
  }


  public List<Product> getAllProductsWithLabels(int page, int size, String query) {
    Pageable pageable = PageRequest.of(page, size);
    return productRepository.findAllProductsWithLabels(pageable, query);

  }

  public List<Product> getAllProductsByCategoryName(String categoryName) {
    Category category = categoryRepository.getCategoryByName(categoryName)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    return productRepository.findByCategoryId(category.getId());
  }

  public List<Product> getAllProductsWithoutPagination() {
    return productRepository.findAll();
  }
}
