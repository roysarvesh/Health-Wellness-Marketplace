package com.wellness.backend.service;


import com.wellness.backend.model.SeverityLevel;
import org.springframework.stereotype.Service;

@Service
public class SeverityAnalyzer {

    public SeverityLevel analyze(String input) {

        if (input == null || input.isBlank()) {
            return SeverityLevel.LOW; // safe default
        }

        int score = 0;
        String text = input.toLowerCase();

        if (text.contains("always") || text.contains("daily")) {
            score += 2;
        }

        if (text.contains("severe") || text.contains("extreme")) {
            score += 2;
        }

        if (text.contains("can't") || text.contains("cannot") || text.contains("unable")) {
            score += 1;
        }

        if (score >= 4) {
            return SeverityLevel.HIGH;
        }

        if (score >= 2) {
            return SeverityLevel.MODERATE;
        }

        return SeverityLevel.LOW;
    }
}
