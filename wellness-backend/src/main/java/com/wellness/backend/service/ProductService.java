package com.wellness.backend.service;

import com.wellness.backend.model.Product;
import com.wellness.backend.model.ProductCategory;
import com.wellness.backend.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    // ---------------------------------------------------------
    // BASIC FETCH
    // ---------------------------------------------------------
    public List<Product> getAll() {
        return repo.findAll();
    }

    public Product getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getByCategory(ProductCategory category) {
        return repo.findByCategory(category);
    }

    // ---------------------------------------------------------
    // SEARCH (keyword only)
    // ---------------------------------------------------------
    public List<Product> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ---------------------------------------------------------
    // ADVANCED SEARCH (keyword + price + category)
    // ---------------------------------------------------------
    public List<Product> searchAdvanced(
            String keyword,
            Double minPrice,
            Double maxPrice,
            ProductCategory category
    ) {
        return repo.findAll().stream()
                .filter(p -> keyword == null || p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .filter(p -> minPrice == null || p.getPrice().doubleValue() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice().doubleValue() <= maxPrice)
                .filter(p -> category == null || p.getCategory() == category)
                .toList();
    }

    // ---------------------------------------------------------
    // PAGINATION + SORTING
    // ---------------------------------------------------------
    public Page<Product> getAllPaged(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return repo.findAll(pageable);
    }

    // ---------------------------------------------------------
    // STOCK VALIDATION
    // ---------------------------------------------------------
    public boolean isInStock(Long productId, int requiredQty) {
        Product p = getById(productId);
        return p.getStock() >= requiredQty;
    }

    // ---------------------------------------------------------
    // RECOMMENDED PRODUCTS (same category)
    // ---------------------------------------------------------
    public List<Product> getRecommendedProducts(Long productId) {
        Product product = getById(productId);
        return repo.findByCategory(product.getCategory()).stream()
                .filter(p -> !p.getId().equals(productId))
                .limit(6)
                .toList();
    }

    // ---------------------------------------------------------
    // ADMIN: ADD / UPDATE / DELETE PRODUCT
    // ---------------------------------------------------------
    public Product addProduct(Product product) {
        return repo.save(product);
    }

    public Product updateProduct(Long id, Product updated) {
        Product existing = getById(id);

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setCategory(updated.getCategory());
        existing.setPrice(updated.getPrice());
        existing.setStock(updated.getStock());
        existing.setImageUrl(updated.getImageUrl());

        return repo.save(existing);
    }

    public void deleteProduct(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        repo.deleteById(id);
    }
}
