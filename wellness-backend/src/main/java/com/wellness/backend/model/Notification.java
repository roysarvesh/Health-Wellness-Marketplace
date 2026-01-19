package com.wellness.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who receives the notification
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // SESSION, ORDER, RECOMMENDATION, SYSTEM
    @Column(nullable = false)
    private String type;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
