package com.wellness.backend.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "practitioner_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PractitionerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                   // _id

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;                 // _userId

    private String specialization;     // _specialization

    @Builder.Default
    private boolean verified = false;
    private Double latitude;
    private Double longitude;

    private String city;
    private String address;
    @Builder.Default
    private Double rating = 0.0; // _rating
}

