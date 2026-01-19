package com.wellness.backend.ai.dto;

import java.util.ArrayList;
import java.util.List;

public class SymptomRequest {

    private String symptom;
    private List<String> symptoms;
    private Integer age;
    private String gender;

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<String> symptoms) {
        this.symptoms = symptoms;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public List<String> resolveSymptoms() {
        List<String> resolved = new ArrayList<>();

        if (symptoms != null && !symptoms.isEmpty()) {
            resolved.addAll(symptoms);
        } else if (symptom != null && !symptom.isBlank()) {
            resolved.add(symptom);
        }

        return resolved;
    }
}
