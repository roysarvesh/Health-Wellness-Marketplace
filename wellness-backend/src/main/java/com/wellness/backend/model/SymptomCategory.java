package com.wellness.backend.model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SymptomCategory {

    ANXIETY("Anxiety"),
    DEPRESSION("Depression"),
    STRESS("Stress"),
    SLEEP("Sleep Issues"),
    FATIGUE("Fatigue"),
    PAIN("Pain"),
    GENERAL("General");

    private final String displayName;

    SymptomCategory(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static SymptomCategory fromString(String value) {
        if (value == null || value.isBlank()) {
            return GENERAL;
        }

        for (SymptomCategory category : SymptomCategory.values()) {
            if (category.name().equalsIgnoreCase(value)
                    || category.displayName.equalsIgnoreCase(value)) {
                return category;
            }
        }
        return GENERAL;
    }
}
