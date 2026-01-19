package com.wellness.backend.scheduler;

import com.wellness.backend.model.TherapySession;
import com.wellness.backend.repository.TherapySessionRepository;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@EnableScheduling
@Configuration
@Component
public class SessionReminderScheduler {

    private final TherapySessionRepository repository;

    public SessionReminderScheduler(TherapySessionRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void sendReminders() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24 = now.plusHours(24);

        List<TherapySession> sessions =
                repository.findSessionsForReminders(now, next24);

        for (TherapySession s : sessions) {
            System.out.println(
                "🔔 Reminder sent to " +
                s.getPatient().getEmail() +
                " for session at " +
                s.getSessionTime()
            );
        }
    }
}
