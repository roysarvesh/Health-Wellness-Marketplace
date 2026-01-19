package com.wellness.backend.service;

import com.wellness.backend.dto.PractitionerResponse;
import com.wellness.backend.dto.PractitionerUpdateRequest;
import com.wellness.backend.dto.VerifyResponse;
import com.wellness.backend.exception.ResourceNotFoundException;
import com.wellness.backend.model.PractitionerProfile;
import com.wellness.backend.model.User;
import com.wellness.backend.repository.PractitionerProfileRepository;
import com.wellness.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PractitionerService {

    private final PractitionerProfileRepository practitionerRepo;
    private final UserRepository userRepository;

    public PractitionerService(PractitionerProfileRepository practitionerRepo,
                               UserRepository userRepository) {
        this.practitionerRepo = practitionerRepo;
        this.userRepository = userRepository;
    }

    /* ===================== READ OPERATIONS ===================== */

    public List<PractitionerResponse> listAllPractitioners() {
        return practitionerRepo.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PractitionerResponse getPractitionerById(Long id) {
        PractitionerProfile profile = practitionerRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Practitioner not found with id: " + id)
                );
        return toResponse(profile);
    }

    /* ===================== UPDATE PROFILE ===================== */

    @Transactional
    public PractitionerResponse updatePractitionerProfile(
            Long practitionerProfileId,
            PractitionerUpdateRequest req,
            String userEmail
    ) {
        PractitionerProfile profile = practitionerRepo.findById(practitionerProfileId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Practitioner not found with id: " + practitionerProfileId)
                );

        User user = profile.getUser();

        // Authorization check
        if (!user.getEmail().equals(userEmail)) {
            throw new IllegalStateException("You are not authorized to update this practitioner profile");
        }

        // Update practitioner fields
        if (req.getSpecialization() != null) {
            profile.setSpecialization(req.getSpecialization());
        }

        // Update user fields
        if (req.getBio() != null) {
            user.setBio(req.getBio());
        }

        // Save explicitly (safe)
        userRepository.save(user);
        PractitionerProfile savedProfile = practitionerRepo.save(profile);

        return toResponse(savedProfile);
    }

    /* ===================== VERIFY PRACTITIONER ===================== */

    @Transactional
    public VerifyResponse verifyPractitioner(Long practitionerProfileId) {
        PractitionerProfile profile = practitionerRepo.findById(practitionerProfileId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Practitioner not found with id: " + practitionerProfileId)
                );

        profile.setVerified(true);
        practitionerRepo.save(profile);

        return new VerifyResponse(profile.getId(), true);
    }

    /* ===================== MAPPER ===================== */

    private PractitionerResponse toResponse(PractitionerProfile profile) {
        User user = profile.getUser();

        return PractitionerResponse.builder()
                // From User entity
                .name(user.getName())
                .email(user.getEmail())
                .password(null)        // NEVER return password
                .role(user.getRole().name())
                .bio(user.getBio())

                // From PractitionerProfile entity
                .specialization(profile.getSpecialization())
                .latitude(profile.getLatitude())
                .longitude(profile.getLongitude())
                .city(profile.getCity())
                .address(profile.getAddress())
                .rating(profile.getRating())

                .build();
    }
}
