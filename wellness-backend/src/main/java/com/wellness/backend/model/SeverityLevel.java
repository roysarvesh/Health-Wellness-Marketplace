package com.wellness.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SeverityLevel {

    LOW("Low"),
    MODERATE("Moderate"),
    HIGH("High");

    private final String displayName;

    SeverityLevel(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static SeverityLevel fromString(String value) {
        if (value == null || value.isBlank()) {
            return LOW; 
        }
        for (SeverityLevel level : SeverityLevel.values()) {
            if (level.name().equalsIgnoreCase(value)
                    || level.displayName.equalsIgnoreCase(value)) {
                return level;
            }
        }
        return LOW;
    }
}