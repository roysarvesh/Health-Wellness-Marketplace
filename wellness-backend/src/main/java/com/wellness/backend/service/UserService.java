package com.wellness.backend.service;

import com.wellness.backend.dto.UserProfileResponse;
import com.wellness.backend.exception.ResourceNotFoundException;
import com.wellness.backend.model.Role;
import com.wellness.backend.model.User;
import com.wellness.backend.repository.PractitionerProfileRepository;
import com.wellness.backend.repository.UserRepository;
import com.wellness.backend.repository.TherapySessionRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PractitionerProfileRepository practitionerProfileRepository;
    private final TherapySessionRepository therapySessionRepository;

    public UserService(UserRepository userRepository,
                       PractitionerProfileRepository practitionerProfileRepository,
                       TherapySessionRepository therapySessionRepository) {
        this.userRepository = userRepository;
        this.practitionerProfileRepository = practitionerProfileRepository;
        this.therapySessionRepository = therapySessionRepository;
    }

    // -------------------------------------------------
    // GET USER PROFILE BY EMAIL
    // -------------------------------------------------
    public UserProfileResponse getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found: " + email));

        return mapToProfileResponse(user);
    }

    // -------------------------------------------------
    // GET USER PROFILE BY ID
    // -------------------------------------------------
    public UserProfileResponse getUserProfileById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found: " + id));

        return mapToProfileResponse(user);
    }

    // -------------------------------------------------
    // ADMIN: GET ALL USERS
    // -------------------------------------------------
    public List<UserProfileResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToProfileResponse)
                .collect(Collectors.toList());
    }


    // -------------------------------------------------
    // DELETE OWN PROFILE (PATIENT / PRACTITIONER)
    // -------------------------------------------------
    public void deleteMyProfile(User currentUser) {

        if (currentUser.getRole() == Role.ADMIN) {
            throw new AccessDeniedException("Admin cannot delete own account");
        }

        // ✨ Check active sessions before deleting (as patient)
        if (therapySessionRepository.existsByPatient_Id(currentUser.getId())) {
            throw new IllegalStateException("You cannot delete your account. Active therapy sessions exist.");
        }

        if (currentUser.getRole() == Role.PRACTITIONER) {
            var practitioner = practitionerProfileRepository.findByUser(currentUser);

            // ✨ Check active sessions assigned to practitioner
            if (practitioner.isPresent() &&
                therapySessionRepository.existsByPractitioner_Id(practitioner.get().getId())) {
                throw new IllegalStateException("You cannot delete your account. Active sessions assigned to you exist.");
            }

            practitioner.ifPresent(practitionerProfileRepository::delete);
        }

        userRepository.delete(currentUser);
    }


    // -------------------------------------------------
    // ADMIN: DELETE ANY USER
    // -------------------------------------------------
    public void adminDeleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        if (user.getRole() == Role.ADMIN) {
            throw new AccessDeniedException("Admin cannot delete another admin");
        }

        // ✨ Check active sessions for patient
        if (therapySessionRepository.existsByPatient_Id(user.getId())) {
            throw new IllegalStateException("Cannot delete user. User has active therapy sessions.");
        }

        // ✨ Check active sessions for practitioner
        if (user.getRole() == Role.PRACTITIONER) {
            var practitioner = practitionerProfileRepository.findByUser(user);

            if (practitioner.isPresent() &&
                therapySessionRepository.existsByPractitioner_Id(practitioner.get().getId())) {
                throw new IllegalStateException("Cannot delete practitioner. They have active therapy sessions.");
            }

            practitioner.ifPresent(practitionerProfileRepository::delete);
        }

        userRepository.delete(user);
    }


    // -------------------------------------------------
    // HELPER: ENTITY → DTO
    // -------------------------------------------------
    private UserProfileResponse mapToProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getBio()
        );
    }
}
