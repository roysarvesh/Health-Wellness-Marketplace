package com.wellness.backend.repository;

import com.wellness.backend.model.Product;
import com.wellness.backend.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Browse products by category
    List<Product> findByCategory(ProductCategory category);

    // Search products by name (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String keyword);
}
