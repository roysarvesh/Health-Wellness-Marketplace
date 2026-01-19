package com.wellness.backend.controller;

import com.wellness.backend.model.SessionMode;
import com.wellness.backend.model.TherapySession;
import com.wellness.backend.model.TherapyType;
import com.wellness.backend.model.User;
import com.wellness.backend.security.CustomUserDetails;
import com.wellness.backend.service.TherapySessionService;

import io.jsonwebtoken.lang.Arrays;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.wellness.backend.dto.BookSessionRequest;
import com.wellness.backend.dto.RescheduleSessionRequest;
import com.wellness.backend.dto.UpcomingSessionResponse;
@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class TherapySessionController {

    private final TherapySessionService therapySessionService;

    public TherapySessionController(TherapySessionService therapySessionService) {
        this.therapySessionService = therapySessionService;
    }
    @PostMapping("/book")
    public ResponseEntity<TherapySession> bookSession(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BookSessionRequest request
    ) {
        Long patientId = userDetails.getUser().getId();

        LocalDateTime sessionTime =
                LocalDateTime.parse(request.getSessionTime());

        TherapySession session = therapySessionService.bookSession(
                patientId,
                request.getPractitionerId(),
                sessionTime,
                SessionMode.valueOf(request.getMode())
        );

        return ResponseEntity.ok(session);
    }


    @GetMapping("/history")
    public ResponseEntity<List<TherapySession>> sessionHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long patientId = userDetails.getUser().getId();
        return ResponseEntity.ok(
                therapySessionService.getSessionHistory(patientId)
        );
    }
   
    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelSession(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long patientId = userDetails.getUser().getId();
        therapySessionService.cancelSession(id, patientId);
        return ResponseEntity.ok("Session cancelled successfully");
    }
    
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<TherapySession> rescheduleSession(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RescheduleSessionRequest request
    ) {
        Long patientId = userDetails.getUser().getId();

        LocalDateTime newTime = LocalDateTime.parse(request.getNewSessionTime());

        TherapySession session =
                therapySessionService.rescheduleSession(id, patientId, newTime);

        return ResponseEntity.ok(session);
    }
    @GetMapping("/calendar")
    public List<TherapySession> getCalendarSessions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String start,
            @RequestParam String end
    ) {
        User user = userDetails.getUser();

        // 🔒 Trim to remove <EOL>, spaces, hidden characters
        LocalDate startDate = LocalDate.parse(start.trim());
        LocalDate endDate = LocalDate.parse(end.trim());

        return therapySessionService.getCalendarSessions(
                user,
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );
    }
    @GetMapping("/patient/upcoming")
    public ResponseEntity<List<UpcomingSessionResponse>> patientUpcomingSessions(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                therapySessionService.getUpcomingSessionsForPatient(
                        userDetails.getUser()
                )
        );
    }

    @GetMapping("/practitioner/upcoming")
    public ResponseEntity<List<UpcomingSessionResponse>> practitionerUpcomingSessions(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                therapySessionService.getUpcomingSessionsForPractitioner(
                        userDetails.getUser()
                )
        );
    }


    @GetMapping
    @RequestMapping("/therapy-types")
    public List<String> getTherapyTypes() {
        return Stream.of(TherapyType.values())
                     .map(Enum::name)
                     .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TherapySession> getSessionDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(
                therapySessionService.getSessionDetail(id, userId)
        );
    }

    @GetMapping("/availability")
    public ResponseEntity<List<LocalDateTime>> getAvailability(
            @RequestParam Long practitionerId,
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(
                therapySessionService.getAvailableSlots(practitionerId, date)
        );
    }

}
