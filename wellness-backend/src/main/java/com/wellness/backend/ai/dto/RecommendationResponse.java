package com.wellness.backend.ai.dto;

import com.wellness.backend.model.SeverityLevel;
import com.wellness.backend.model.SymptomCategory;

import java.util.List;
import java.util.Set;

public class RecommendationResponse {

    private Set<SymptomCategory> categories;
    private SeverityLevel severity;
    private boolean requiresMedicalAttention;

    private String summary;
    private List<String> recommendedTherapies;
    private List<String> lifestyleTips;
    private List<String> products;

    private String disclaimer;



    public RecommendationResponse() {
        // default constructor for Jackson
    }

    public RecommendationResponse(
            Set<SymptomCategory> categories,
            SeverityLevel severity,
            boolean requiresMedicalAttention,
            String summary,
            List<String> recommendedTherapies,
            List<String> lifestyleTips,
            List<String> products,
            String disclaimer
    ) {
        this.categories = categories;
        this.severity = severity;
        this.requiresMedicalAttention = requiresMedicalAttention;
        this.summary = summary;
        this.recommendedTherapies = recommendedTherapies;
        this.lifestyleTips = lifestyleTips;
        this.products = products;
        this.disclaimer = disclaimer;
    }

    // ===== Getters & Setters =====

    public Set<SymptomCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<SymptomCategory> categories) {
        this.categories = categories;
    }

    public SeverityLevel getSeverity() {
        return severity;
    }

    public void setSeverity(SeverityLevel severity) {
        this.severity = severity;
    }

    public boolean isRequiresMedicalAttention() {
        return requiresMedicalAttention;
    }

    public void setRequiresMedicalAttention(boolean requiresMedicalAttention) {
        this.requiresMedicalAttention = requiresMedicalAttention;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getRecommendedTherapies() {
        return recommendedTherapies;
    }

    public void setRecommendedTherapies(List<String> recommendedTherapies) {
        this.recommendedTherapies = recommendedTherapies;
    }

    public List<String> getLifestyleTips() {
        return lifestyleTips;
    }

    public void setLifestyleTips(List<String> lifestyleTips) {
        this.lifestyleTips = lifestyleTips;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
}
