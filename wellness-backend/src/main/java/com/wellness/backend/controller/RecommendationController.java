package com.wellness.backend.controller;

import com.wellness.backend.model.Recommendation;
import com.wellness.backend.security.CustomUserDetails;
import com.wellness.backend.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Recommendation> generate(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam String symptom
    ) {
        return ResponseEntity.ok(
                service.generate(
                        user.getUser().getId(),
                        symptom
                )
        );
    }

    @GetMapping("/history")
    public ResponseEntity<List<Recommendation>> history(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                service.history(
                        user.getUser().getId()
                )
        );
    }
}
