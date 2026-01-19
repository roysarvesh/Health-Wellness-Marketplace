package com.wellness.backend.controller;

import com.wellness.backend.dto.ReviewRequest;
import com.wellness.backend.security.CustomUserDetails;
import com.wellness.backend.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<String> addReview(
            @PathVariable Long productId,
            @RequestBody ReviewRequest req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        reviewService.addReview(
                user.getUser(),          // User
                productId,               // Product ID
                req.getRating(),         // int rating
                req.getComment()         // String comment
        );

        return ResponseEntity.ok("Review added successfully");
    }
}
