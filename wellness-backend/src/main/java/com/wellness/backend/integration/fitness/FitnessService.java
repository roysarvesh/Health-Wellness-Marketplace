package com.wellness.backend.integration.fitness;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FitnessService {


    public Map<String, Object> getActivitySummary(Long userId) {

        Map<String, Object> summary = new HashMap<>();
        summary.put("steps", 7500);
        summary.put("sleepHours", 6.5);
        summary.put("heartRate", 72);

        return summary;
    }
}
