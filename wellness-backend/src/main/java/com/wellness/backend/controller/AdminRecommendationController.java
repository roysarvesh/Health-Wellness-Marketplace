package com.wellness.backend.controller;

import com.wellness.backend.model.Recommendation;
import com.wellness.backend.repository.RecommendationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/recommendations")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRecommendationController {

    private final RecommendationRepository repo;

    public AdminRecommendationController(RecommendationRepository repo) {
        this.repo = repo;
    }


    @GetMapping
    public ResponseEntity<List<Recommendation>> getAllRecommendations() {
        return ResponseEntity.ok(repo.findAll());
    }
}
