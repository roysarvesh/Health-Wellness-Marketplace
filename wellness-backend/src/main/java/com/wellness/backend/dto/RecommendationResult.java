package com.wellness.backend.dto;

import com.wellness.backend.model.SeverityLevel;
import com.wellness.backend.model.SymptomCategory;

import java.util.List;
import java.util.Set;

public class RecommendationResult {

    private Set<SymptomCategory> categories;
    private List<String> therapies;
    private SeverityLevel severity;
    private boolean requiresMedicalAttention;

    public RecommendationResult(
            Set<SymptomCategory> categories,
            List<String> therapies,
            SeverityLevel severity,
            boolean requiresMedicalAttention
    ) {
        this.categories = categories;
        this.therapies = therapies;
        this.severity = severity;
        this.requiresMedicalAttention = requiresMedicalAttention;
    }


    public Set<SymptomCategory> getCategories() {
        return categories;
    }

    public List<String> getTherapies() {
        return therapies;
    }

    public SeverityLevel getSeverity() {
        return severity;
    }

    public boolean isRequiresMedicalAttention() {
        return requiresMedicalAttention;
    }
}
