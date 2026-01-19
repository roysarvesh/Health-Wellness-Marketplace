package com.wellness.backend.service;

import com.wellness.backend.model.Recommendation;
import com.wellness.backend.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecommendationService {

    private final RecommendationRepository repo;
    private final AiRecommendationService aiService;

    public RecommendationService(
            RecommendationRepository repo,
            AiRecommendationService aiService
    ) {
        this.repo = repo;
        this.aiService = aiService;
    }

    public Recommendation generate(Long userId, String symptom) {

        String therapy = aiService.suggestTherapy(symptom);

        Recommendation rec = new Recommendation();
        rec.setUserId(userId);
        rec.setSymptom(symptom);
        rec.setSuggestedTherapy(therapy);
        rec.setSourceApi("AI_FDA_ENGINE");
        rec.setCreatedAt(LocalDateTime.now());

        return repo.save(rec);
    }

    public List<Recommendation> history(Long userId) {
        return repo.findByUserId(userId);
    }
}
