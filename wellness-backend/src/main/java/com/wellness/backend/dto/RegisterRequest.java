package com.wellness.backend.dto;

import com.wellness.backend.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;           // PATIENT or PRACTITIONER
    private String bio;
    private String specialization; // only if PRACTITIONER
}
