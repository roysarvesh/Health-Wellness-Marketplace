package com.wellness.backend.service;

import com.wellness.backend.dto.SessionHistoryResponse;
import com.wellness.backend.dto.UpcomingSessionResponse;
import com.wellness.backend.integration.GoogleCalendarService.java.GoogleCalendarService;
import com.wellness.backend.model.*;
import com.wellness.backend.repository.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class TherapySessionService {

    private final TherapySessionRepository therapySessionRepository;
    private final UserRepository userRepository;
    private final PractitionerProfileRepository practitionerProfileRepository;
    private final GoogleCalendarService googleCalendarService;

    public TherapySessionService(
            TherapySessionRepository therapySessionRepository,
            UserRepository userRepository,
            PractitionerProfileRepository practitionerProfileRepository,
            GoogleCalendarService googleCalendarService
    ) {
        this.therapySessionRepository = therapySessionRepository;
        this.userRepository = userRepository;
        this.practitionerProfileRepository = practitionerProfileRepository;
        this.googleCalendarService = googleCalendarService;
    }

    // -------------------------------------------------
    // BOOK SESSION
    // -------------------------------------------------
    public TherapySession bookSession(
            Long patientId,
            Long practitionerId,
            LocalDateTime sessionTime,
            SessionMode mode
    ) {
        validateSessionTime(sessionTime);

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        PractitionerProfile practitioner = practitionerProfileRepository
                .findById(practitionerId)
                .orElseThrow(() -> new RuntimeException("Practitioner not found"));

        validateSlotAvailability(practitioner, sessionTime);

        TherapySession session = TherapySession.builder()
                .patient(patient)
                .practitioner(practitioner)
                .sessionTime(sessionTime)
                .mode(mode)
                .status(SessionStatus.BOOKED)
                .build();

        session = therapySessionRepository.save(session);
        googleCalendarService.createEvent(session);

        return session;
    }

    // -------------------------------------------------
    // UPCOMING SESSIONS — PATIENT
    // -------------------------------------------------
    public List<UpcomingSessionResponse> getUpcomingSessionsForPatient(User patient) {

        LocalDateTime now = LocalDateTime.now();

        return therapySessionRepository
                .findByUserAndSessionTimeAfter(patient, now)
                .stream()
                .filter(ts ->
                        ts.getStatus() == SessionStatus.BOOKED ||
                        ts.getStatus() == SessionStatus.RESCHEDULED
                )
                .map(this::mapToUpcomingDTO)
                .toList();
    }

    // -------------------------------------------------
    // UPCOMING SESSIONS — PRACTITIONER
    // -------------------------------------------------
    public List<UpcomingSessionResponse> getUpcomingSessionsForPractitioner(User practitionerUser) {

        PractitionerProfile practitioner =
                practitionerProfileRepository.findByUser(practitionerUser)
                        .orElseThrow(() -> new RuntimeException("Practitioner profile not found"));

        LocalDateTime now = LocalDateTime.now();

        return therapySessionRepository
                .findByPractitionerAndSessionTimeAfter(practitionerUser, now)
                .stream()
                .filter(ts ->
                        ts.getStatus() == SessionStatus.BOOKED ||
                        ts.getStatus() == SessionStatus.RESCHEDULED
                )
                .map(this::mapToUpcomingDTO)
                .toList();
    }

    public List<TherapySession> getSessionHistory(Long patientId) {

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Mark old sessions completed first
        updateCompletedSessions();

        return therapySessionRepository.findAll().stream()
                .filter(ts -> ts.getPatient().getId().equals(patient.getId()))
                .filter(ts -> ts.getSessionTime().isBefore(LocalDateTime.now()))
                .toList();
    }
    private SessionHistoryResponse mapToHistoryDTO(TherapySession session) {

        return new SessionHistoryResponse(
                session.getId(),
                session.getSessionTime(),
                session.getStatus().name(),
                session.getMode() != null ? session.getMode().name() : null,
                session.getTherapyType() != null ? session.getTherapyType().name() : null,
                session.getPractitioner().getId(),
                session.getPractitioner().getUser().getName(),
                session.getPatient().getId(),
                session.getPatient().getName()
        );
    }		

    // -------------------------------------------------
    // CANCEL SESSION
    // -------------------------------------------------
    public void cancelSession(Long sessionId, Long patientId) {

        TherapySession session = therapySessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getPatient().getId().equals(patientId)) {
            throw new AccessDeniedException("Not allowed to cancel this session");
        }

        session.setStatus(SessionStatus.CANCELLED);
        therapySessionRepository.save(session);

        googleCalendarService.deleteEvent(session);
    }

    // -------------------------------------------------
    // RESCHEDULE SESSION
    // -------------------------------------------------
    public TherapySession rescheduleSession(
            Long sessionId,
            Long patientId,
            LocalDateTime newTime
    ) {
        validateSessionTime(newTime);

        TherapySession session = therapySessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getPatient().getId().equals(patientId)) {
            throw new AccessDeniedException("Not allowed");
        }

        validateSlotAvailability(session.getPractitioner(), newTime);

        session.setSessionTime(newTime);
        session.setStatus(SessionStatus.RESCHEDULED);

        session = therapySessionRepository.save(session);
        googleCalendarService.updateEvent(session);

        return session;
    }

    // -------------------------------------------------
    // CALENDAR VIEW — PATIENT & PRACTITIONER
    // -------------------------------------------------
    public List<TherapySession> getCalendarSessions(
            User user,
            LocalDateTime start,
            LocalDateTime end
    ) {
        if (user.getRole() == Role.PATIENT) {
            return therapySessionRepository
                    .findPatientSessionsBetween(user, start, end);
        }

        if (user.getRole() == Role.PRACTITIONER) {
            return therapySessionRepository
                    .findPractitionerSessionsBetween(user, start, end);
        }

        throw new AccessDeniedException("Invalid role");
    }

    // -------------------------------------------------
    // SESSION DETAIL
    // -------------------------------------------------
    public TherapySession getSessionDetail(Long sessionId, Long userId) {

        TherapySession session = therapySessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        boolean isPatient = session.getPatient().getId().equals(userId);
        boolean isPractitioner =
                session.getPractitioner().getUser().getId().equals(userId);

        if (!isPatient && !isPractitioner) {
            throw new AccessDeniedException("Not authorized");
        }

        return session;
    }

    // -------------------------------------------------
    // AVAILABLE SLOTS
    // -------------------------------------------------
    public List<LocalDateTime> getAvailableSlots(
            Long practitionerId,
            LocalDate date
    ) {
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return List.of();
        }

        List<LocalDateTime> booked =
                therapySessionRepository.findBookedSlots(practitionerId, date);

        List<LocalDateTime> available = new ArrayList<>();

        LocalDateTime start = date.atTime(8, 0);
        LocalDateTime end = date.atTime(22, 0);

        while (start.isBefore(end)) {
            if (!booked.contains(start)) {
                available.add(start);
            }
            start = start.plusMinutes(30);
        }

        return available;
    }

    // -------------------------------------------------
    // DTO MAPPER
    // -------------------------------------------------
    private UpcomingSessionResponse mapToUpcomingDTO(TherapySession session) {
        return new UpcomingSessionResponse(
                session.getId(),
                session.getSessionTime(),
                session.getMode() != null ? session.getMode().name() : null,
                session.getStatus().name(),
                session.getPractitioner().getId(),
                session.getPractitioner().getUser().getName(),
                session.getPatient().getId(),
                session.getPatient().getName(),
                session.getTherapyType() != null
                        ? session.getTherapyType().name()
                        : null
        );
    }

    // -------------------------------------------------
    // VALIDATIONS
    // -------------------------------------------------
    private void validateSessionTime(LocalDateTime time) {

        if (time.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalStateException("Sessions not available on Sundays");
        }

        LocalTime t = time.toLocalTime();
        if (t.isBefore(LocalTime.of(8, 0)) || t.isAfter(LocalTime.of(22, 0))) {
            throw new IllegalStateException(
                    "Sessions allowed only between 8:00 AM and 10:00 PM"
            );
        }
    }
    public void updateCompletedSessions() {
        LocalDateTime now = LocalDateTime.now();

        List<TherapySession> pastSessions =
                therapySessionRepository.findByStatus(SessionStatus.BOOKED)
                        .stream()
                        .filter(ts -> ts.getSessionTime().isBefore(now))
                        .toList();

        pastSessions.forEach(ts -> ts.setStatus(SessionStatus.COMPLETED));
        therapySessionRepository.saveAll(pastSessions);
    }
    private void validateSlotAvailability(
            PractitionerProfile practitioner,
            LocalDateTime time
    ) {
        LocalDateTime start = time.minusMinutes(30);
        LocalDateTime end = time.plusMinutes(30);

        if (!therapySessionRepository
                .findConflictingSessions(practitioner, start, end)
                .isEmpty()) {
            throw new IllegalStateException("Time slot already booked");
        }
    }
}
