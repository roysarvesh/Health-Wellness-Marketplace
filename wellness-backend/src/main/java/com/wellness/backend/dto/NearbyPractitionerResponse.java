package com.wellness.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NearbyPractitionerResponse {

    private Long practitionerId;
    private String name;
    private String email;
    private String bio;

    private String specialization;
    private boolean verified;
    private double rating;

    private double latitude;
    private double longitude;

    private String city;
    private String address;

    private double distanceKm;
}
