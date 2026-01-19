package com.wellness.backend.controller;

import com.wellness.backend.notification.NotificationService;
import com.wellness.backend.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> myNotifications(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                service.getUserNotifications(user.getUser().getId())
        );
    }


    @GetMapping("/unread-count")
    public ResponseEntity<Long> unreadCount(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                service.unreadCount(user.getUser().getId())
        );
    }


    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markRead(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        service.markAsRead(id, user.getUser().getId());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/test")
    public ResponseEntity<String> testNotification() {
        service.notifyUser(1L, "Your AI recommendations are ready!");
        return ResponseEntity.ok("Notification sent");
    }
}
