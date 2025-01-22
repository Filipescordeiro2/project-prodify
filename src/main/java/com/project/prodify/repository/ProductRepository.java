package com.project.prodify.repository;

import com.project.prodify.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional <Product> findByName(String name);
    Optional<Product> findBySKU(String SKU);
}
