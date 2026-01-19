package com.wellness.backend.ai.engine;

import com.wellness.backend.ai.dto.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RuleBasedRecommendationEngine implements RecommendationEngine {

    @Override
    public RecommendationResponse recommend(SymptomRequest request) {

        Set<String> therapies = new LinkedHashSet<>();
        Set<String> tips = new LinkedHashSet<>();
        Set<String> products = new LinkedHashSet<>();

        if (request.getSymptoms() == null || request.getSymptoms().isEmpty()) {
            return defaultResponse();
        }

        for (String symptom : request.getSymptoms()) {

            if (symptom == null) continue;

            switch (symptom.toLowerCase().trim()) {

                case "stress" -> {
                    therapies.add("Meditation Therapy");
                    therapies.add("Yoga Therapy");
                    tips.add("Practice breathing exercises daily");
                    tips.add("Take short breaks during work");
                    products.add("Aromatherapy Oils");
                }

                case "insomnia" -> {
                    therapies.add("Sleep Therapy");
                    tips.add("Avoid screens 1 hour before sleep");
                    tips.add("Maintain a consistent sleep schedule");
                    products.add("Melatonin Supplements");
                }

                case "anxiety" -> {
                    therapies.add("Cognitive Behavioral Therapy");
                    tips.add("Limit caffeine intake");
                    tips.add("Practice mindfulness");
                    products.add("Herbal Tea");
                }

                default -> {
                    tips.add("Consult a healthcare professional for persistent symptoms");
                }
            }
        }

        RecommendationResponse response = new RecommendationResponse();
        response.setSummary("Personalized wellness recommendations based on your symptoms");
        response.setRecommendedTherapies(new ArrayList<>(therapies));
        response.setLifestyleTips(new ArrayList<>(tips));
        response.setProducts(new ArrayList<>(products));

        return response;
    }

    private RecommendationResponse defaultResponse() {
        RecommendationResponse response = new RecommendationResponse();
        response.setSummary("No symptoms provided. Please share your symptoms for better recommendations.");
        response.setRecommendedTherapies(List.of("General Wellness Guidance"));
        response.setLifestyleTips(List.of(
                "Maintain a balanced diet",
                "Exercise regularly",
                "Get adequate sleep"
        ));
        response.setProducts(List.of("Multivitamin Supplements"));
        return response;
    }
}
