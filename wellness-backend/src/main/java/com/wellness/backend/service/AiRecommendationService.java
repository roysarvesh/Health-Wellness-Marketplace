package com.wellness.backend.service;
import com.wellness.backend.integration.openfda.OpenFdaClient;
import com.wellness.backend.integration.openfda.OpenFdaResponse;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AiRecommendationService {

    private final OpenFdaClient fdaClient;

    // Ordered rules (important for priority)
    private static final Map<String, String> RULES = new LinkedHashMap<>();

    static {
        RULES.put("back pain", "Physiotherapy");
        RULES.put("stress", "Meditation / Ayurveda");
        RULES.put("joint", "Chiropractic");
        RULES.put("digestion", "Ayurveda");
    }

    public AiRecommendationService(OpenFdaClient fdaClient) {
        this.fdaClient = fdaClient;
    }

    public String suggestTherapy(String symptom) {

        if (symptom == null || symptom.trim().isEmpty()) {
            return "General Wellness Consultation";
        }

        String normalized = symptom.toLowerCase();

        OpenFdaResponse fdaResponse =
                fdaClient.searchDrug(normalized).block();

        if (fdaResponse != null
                && fdaResponse.getResults() != null
                && !fdaResponse.getResults().isEmpty()) {

            return "Consult doctor before therapy. FDA flagged medications exist.";
        }

        for (Map.Entry<String, String> rule : RULES.entrySet()) {
            if (normalized.contains(rule.getKey())) {
                return rule.getValue();
            }
        }
        return "General Wellness Consultation";
    }
}
