package com.wellness.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UpcomingSessionResponse {
    private Long sessionId;
    private LocalDateTime sessionTime;
    private String mode;
    private String status;

    private Long practitionerId;
    private String practitionerName;

    private Long patientId;
    private String patientName;

    private String therapyType;
}
