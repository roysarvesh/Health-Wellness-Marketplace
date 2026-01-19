package com.wellness.backend.service;
import com.wellness.backend.model.SymptomCategory;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class NlpProcessingService {

    private static final Map<String, SymptomCategory> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("anxiety", SymptomCategory.ANXIETY);
        KEYWORDS.put("panic", SymptomCategory.ANXIETY);

        KEYWORDS.put("stress", SymptomCategory.STRESS);

        KEYWORDS.put("sad", SymptomCategory.DEPRESSION);
        KEYWORDS.put("depressed", SymptomCategory.DEPRESSION);

        KEYWORDS.put("insomnia", SymptomCategory.SLEEP);
        KEYWORDS.put("sleep", SymptomCategory.SLEEP);

        KEYWORDS.put("tired", SymptomCategory.FATIGUE);
        KEYWORDS.put("fatigue", SymptomCategory.FATIGUE);

        KEYWORDS.put("pain", SymptomCategory.PAIN);
    }

    public Set<SymptomCategory> extractCategories(String input) {

        if (input == null || input.isBlank()) {
            return Set.of(SymptomCategory.GENERAL);
        }

        String text = input.toLowerCase();
        Set<SymptomCategory> categories = new HashSet<>();

        for (Map.Entry<String, SymptomCategory> entry : KEYWORDS.entrySet()) {
            if (text.contains(entry.getKey())) {
                categories.add(entry.getValue());
            }
        }

        return categories.isEmpty()
                ? Set.of(SymptomCategory.GENERAL)
                : categories;
    }
}
