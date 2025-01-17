package pl.lodz.pl.it.mopa.repository;

import pl.lodz.pl.it.mopa.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Product findByEan(String ean);

    @Query("SELECT p FROM Product p JOIN p.label l WHERE lower(p.productName) LIKE lower(CONCAT('%', :query, '%')) ORDER BY p.productName ASC")
    List<Product> findAllProductsWithLabels(Pageable pageable, @Param("query") String query);

    List<Product> findByCategoryId(UUID category_id);


}
