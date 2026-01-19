package com.wellness.backend.model;

public enum Role {
    PATIENT,
    PRACTITIONER,
    ADMIN;

    public String getName() {
        return this.name();
    }
}
