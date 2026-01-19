package com.wellness.backend.repository;

import com.wellness.backend.model.Review;
import com.wellness.backend.model.Product;
import com.wellness.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Reviews for a product
    List<Review> findByProduct(Product product);

    // Ensure one review per user per product
    Optional<Review> findByProductAndUser(Product product, User user);
}
