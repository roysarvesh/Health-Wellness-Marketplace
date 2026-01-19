package com.wellness.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PractitionerResponse {

    private String name;
    private String email;
    private String password;
    private String role;

    private String bio;

    private String specialization;
    private Double latitude;
    private Double longitude;
    private String city;
    private String address;
    private Double rating;// String because we are returning role.name()
}
