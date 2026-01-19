package com.wellness.backend.integration.GoogleCalendarService.java;

import org.springframework.stereotype.Service;

import com.wellness.backend.model.TherapySession;


@Service
public class GoogleCalendarService {

    public void createEvent(TherapySession session) {
        System.out.println("📅 Google Calendar event created for session "
                + session.getId());
    }

    public void updateEvent(TherapySession session) {
        System.out.println("📅 Google Calendar event updated for session "
                + session.getId());
    }

    public void deleteEvent(TherapySession session) {
        System.out.println("📅 Google Calendar event deleted for session "
                + session.getId());
    }
}
