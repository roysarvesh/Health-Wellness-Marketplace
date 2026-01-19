package com.wellness.backend.dto;

import java.time.LocalDateTime;

public class SessionHistoryResponse {

    private Long sessionId;
    private LocalDateTime sessionTime;

    private String status;          // COMPLETED / CANCELLED
    private String mode;            // ONLINE / OFFLINE
    private String therapyType;

    private Long practitionerId;
    private String practitionerName;

    private Long patientId;
    private String patientName;

    public SessionHistoryResponse(
            Long sessionId,
            LocalDateTime sessionTime,
            String status,
            String mode,
            String therapyType,
            Long practitionerId,
            String practitionerName,
            Long patientId,
            String patientName
    ) {
        this.sessionId = sessionId;
        this.sessionTime = sessionTime;
        this.status = status;
        this.mode = mode;
        this.therapyType = therapyType;
        this.practitionerId = practitionerId;
        this.practitionerName = practitionerName;
        this.patientId = patientId;
        this.patientName = patientName;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public LocalDateTime getSessionTime() {
        return sessionTime;
    }

    public String getStatus() {
        return status;
    }

    public String getMode() {
        return mode;
    }

    public String getTherapyType() {
        return therapyType;
    }

    public Long getPractitionerId() {
        return practitionerId;
    }

    public String getPractitionerName() {
        return practitionerName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }
}
