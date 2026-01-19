package com.wellness.backend.controller;

import com.wellness.backend.integration.fitness.FitnessService;
import com.wellness.backend.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/fitness")
public class FitnessController {

    private final FitnessService fitnessService;

    public FitnessController(FitnessService fitnessService) {
        this.fitnessService = fitnessService;
    }


    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                fitnessService.getActivitySummary(user.getUser().getId())
        );
    }
}