package com.wellness.backend.service;

import com.wellness.backend.dto.AuthResponse;
import com.wellness.backend.dto.LoginRequest;
import com.wellness.backend.dto.RegisterRequest;
import com.wellness.backend.model.PractitionerProfile;
import com.wellness.backend.model.Role;
import com.wellness.backend.model.User;
import com.wellness.backend.repository.PractitionerProfileRepository;
import com.wellness.backend.repository.UserRepository;
import com.wellness.backend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PractitionerProfileRepository practitionerProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PractitionerProfileRepository practitionerProfileRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.practitionerProfileRepository = practitionerProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        Role role = request.getRole() == null ? Role.PATIENT : request.getRole();

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .bio(request.getBio())
                .build();

        User savedUser = userRepository.save(user);

        // If practitioner, create profile with specialization & unverified
        if (role == Role.PRACTITIONER) {
            PractitionerProfile profile = PractitionerProfile.builder()
                    .user(savedUser)
                    .specialization(request.getSpecialization())
                    .verified(false)       // to be updated by admin later
                    .rating(0.0)
                    .build();
            practitionerProfileRepository.save(profile);
        }

        String token = jwtService.generateToken(savedUser.getEmail(), new HashMap<>());
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getEmail(), new HashMap<>());

        return new AuthResponse(token);
    }}
