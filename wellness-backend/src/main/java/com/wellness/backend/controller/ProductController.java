package com.wellness.backend.controller;

import com.wellness.backend.model.Product;
import com.wellness.backend.model.ProductCategory;
import com.wellness.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // -------------------------------------------------------------
    // BASIC GET ALL
    // -------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // -------------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // -------------------------------------------------------------
    // CATEGORY FILTER
    // -------------------------------------------------------------
    @GetMapping("/category")
    public ResponseEntity<List<Product>> getByCategory(
            @RequestParam ProductCategory category
    ) {
        return ResponseEntity.ok(service.getByCategory(category));
    }

    // -------------------------------------------------------------
    // SIMPLE SEARCH
    // -------------------------------------------------------------
    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(service.search(keyword));
    }

    // -------------------------------------------------------------
    // ADVANCED SEARCH
    // -------------------------------------------------------------
    @GetMapping("/search/advanced")
    public ResponseEntity<List<Product>> advancedSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) ProductCategory category
    ) {
        return ResponseEntity.ok(
                service.searchAdvanced(keyword, minPrice, maxPrice, category)
        );
    }

    // -------------------------------------------------------------
    // PAGINATION + SORTING
    // -------------------------------------------------------------
    @GetMapping("/paged")
    public ResponseEntity<Page<Product>> getPaged(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(
                service.getAllPaged(page, size, sortBy, direction)
        );
    }

    // -------------------------------------------------------------
    // RECOMMENDED PRODUCTS
    // -------------------------------------------------------------
    @GetMapping("/{id}/recommended")
    public ResponseEntity<List<Product>> getRecommended(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getRecommendedProducts(id));
    }

    // -------------------------------------------------------------
    // STOCK CHECK
    // -------------------------------------------------------------
    @GetMapping("/{id}/stock-check")
    public ResponseEntity<Boolean> isInStock(
            @PathVariable Long id,
            @RequestParam int quantity
    ) {
        return ResponseEntity.ok(service.isInStock(id, quantity));
    }
}
