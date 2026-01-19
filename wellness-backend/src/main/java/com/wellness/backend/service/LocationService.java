package com.wellness.backend.service;

import com.wellness.backend.dto.NearbyPractitionerResponse;
import com.wellness.backend.model.PractitionerProfile;
import com.wellness.backend.model.User;
import com.wellness.backend.repository.PractitionerProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final PractitionerProfileRepository repo;

    public LocationService(PractitionerProfileRepository repo) {
        this.repo = repo;
    }

    public List<NearbyPractitionerResponse> findNearbyPractitioners(
            double lat, double lng, double radiusKm) {

        List<PractitionerProfile> practitioners =
                repo.findNearby(lat, lng, radiusKm);

        return practitioners.stream().map(p -> {

            double distance = calculateDistanceKm(
                    lat, lng,
                    p.getLatitude(), p.getLongitude()
            );

            User u = p.getUser();

            return NearbyPractitionerResponse.builder()
                    .practitionerId(p.getId())
                    .name(u.getName())
                    .email(u.getEmail())
                    .bio(u.getBio())
                    .specialization(p.getSpecialization())
                    .verified(p.isVerified())
                    .rating(p.getRating())
                    .latitude(p.getLatitude())
                    .longitude(p.getLongitude())
                    .city(p.getCity())
                    .address(p.getAddress())
                    .distanceKm(distance)
                    .build();

        }).toList();
    }

    private double calculateDistanceKm(
            double lat1, double lng1,
            double lat2, double lng2) {

        final int R = 6371; // Earth radius in KM

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);

        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
