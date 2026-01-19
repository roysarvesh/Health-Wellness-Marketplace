package com.wellness.backend.controller;
import com.wellness.backend.notification.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/notifications")
@PreAuthorize("hasRole('ADMIN')")
public class AdminNotificationController {

    private final NotificationService service;

    public AdminNotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping("/broadcast")
    public ResponseEntity<String> broadcast(
            @RequestParam String message
    ) {
        service.broadcast(message);
        return ResponseEntity.ok("Broadcast sent successfully");
    }
}
