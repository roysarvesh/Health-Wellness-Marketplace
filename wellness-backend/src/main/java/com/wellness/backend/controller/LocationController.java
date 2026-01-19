package com.wellness.backend.controller;

import com.wellness.backend.dto.NearbyPractitionerResponse;
import com.wellness.backend.service.LocationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/nearby-practitioners")
    public List<NearbyPractitionerResponse> getNearbyPractitioners(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "10") double radiusKm) {

        return locationService.findNearbyPractitioners(lat, lng, radiusKm);
    }
}
