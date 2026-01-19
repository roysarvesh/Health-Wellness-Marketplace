package com.wellness.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TherapySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // PATIENT
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)       // <--- IMPORTANT
    @JsonIgnoreProperties({"password"})
    private User patient;

    // PRACTITIONER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practitioner_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)       // <--- IMPORTANT
    @JsonIgnoreProperties({"user"})
    private PractitionerProfile practitioner;

    private LocalDateTime sessionTime;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @Enumerated(EnumType.STRING)
    private SessionMode mode;

    private String googleEventId;

    @Enumerated(EnumType.STRING)
    private TherapyType therapyType;

}
