package com.wellness.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleMapsService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public GoogleMapsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Reverse geocode latitude & longitude to full address using Google Maps API
     *
     * @param lat latitude value
     * @param lng longitude value
     * @return JSON response from Google Maps API
     */
    public String reverseGeocode(double lat, double lng) {
        String url =
            "https://maps.googleapis.com/maps/api/geocode/json" +
            "?latlng=" + lat + "," + lng +
            "&key=" + apiKey;

        return restTemplate.getForObject(url, String.class);
    }
}
