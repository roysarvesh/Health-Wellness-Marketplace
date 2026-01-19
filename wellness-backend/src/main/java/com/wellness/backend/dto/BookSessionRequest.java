package com.wellness.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSessionRequest {
    private Long practitionerId;
    private String sessionTime;   // ISO string
    private String mode;          // ONLINE / OFFLINE
}