package com.wellness.backend.service;

import com.wellness.backend.model.Product;
import com.wellness.backend.model.Review;
import com.wellness.backend.model.User;
import com.wellness.backend.repository.ProductRepository;
import com.wellness.backend.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            ProductRepository productRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    public Review addReview(User user, Long productId, int rating, String comment) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Prevent duplicate review by same user
        if (reviewRepository.findByProductAndUser(product, user).isPresent()) {
            throw new RuntimeException("You already reviewed this product");
        }

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(rating)
                .comment(comment)
                .build();

        return reviewRepository.save(review);
    }
}
