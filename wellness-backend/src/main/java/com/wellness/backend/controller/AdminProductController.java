
package com.wellness.backend.controller;

import com.wellness.backend.model.Product;
import com.wellness.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasRole('ADMIN')")

public class AdminProductController {

    private final ProductService service;

    public AdminProductController(ProductService service) {
        this.service = service;
    }

    // -------------------- ADD PRODUCT --------------------
    @PostMapping
    public ResponseEntity<Product> addProduct(
            @Valid @RequestBody Product product
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addProduct(product));
    }

    // -------------------- UPDATE PRODUCT --------------------
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product
    ) {
        return ResponseEntity.ok(service.updateProduct(id, product));
    }

    // -------------------- DELETE PRODUCT --------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
