package com.wellness.backend.dto;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String bio;
}
