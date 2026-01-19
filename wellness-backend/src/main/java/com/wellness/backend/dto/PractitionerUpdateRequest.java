package com.wellness.backend.dto;

import lombok.Data;

@Data
public class PractitionerUpdateRequest {
    private String specialization;
    private String bio;
}
