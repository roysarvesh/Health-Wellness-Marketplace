package com.wellness.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyResponse {
    private Long practitionerProfileId;
    private boolean verified;
}
