package com.wellness.backend.controller;

import com.wellness.backend.ai.dto.RecommendationResponse;
import com.wellness.backend.ai.dto.SymptomRequest;
import com.wellness.backend.ai.engine.RecommendationEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ai/recommendations")
@CrossOrigin(origins = "*")
public class AIRecommendationController {

    private final RecommendationEngine engine;

    public AIRecommendationController(RecommendationEngine engine) {
        this.engine = engine;
    }

    @PostMapping
    public ResponseEntity<RecommendationResponse> recommend(
            @RequestBody SymptomRequest request
    ) {
        if (request == null || !hasValidSymptoms(request)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(engine.recommend(request));
    }

    private boolean hasValidSymptoms(SymptomRequest request) {
        List<String> symptoms = request.resolveSymptoms();
        return symptoms != null && !symptoms.isEmpty();
    }
}
