package com.wellness.backend.dto;

import lombok.Data;

@Data
public class QuestionRequest {
    private String title;
    private String description;
}