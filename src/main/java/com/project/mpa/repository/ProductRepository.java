package com.project.mpa.repository;

import com.project.mpa.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Product findByEan(String ean);

    @Query("SELECT p FROM Product p JOIN p.label l ORDER BY p.productName ASC")
    List<Product> findAllProductsWithLabels(Pageable pageable);

}
