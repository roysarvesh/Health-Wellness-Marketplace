package com.wellness.backend.integration.fitness;

import org.springframework.stereotype.Service;

@Service
public class FitnessApiClient {

    public int getDailySteps(Long userId) {
        return 8500; // mocked
    }

    public int getSleepHours(Long userId) {
        return 6;
    }
}
