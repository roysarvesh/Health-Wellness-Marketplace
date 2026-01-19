package com.wellness.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String symptom;

    @Column(name = "suggested_therapy", nullable = false, length = 500)
    private String suggestedTherapy;

    @Column(name = "source_api", nullable = false)
    private String sourceApi;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
