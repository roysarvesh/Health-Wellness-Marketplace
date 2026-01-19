package com.wellness.backend.integration.who;

import org.springframework.stereotype.Service;

@Service
public class WhoApiClient {

    public String getHealthGuidelines(String topic) {
        return "WHO guideline for " + topic + ": Maintain a balanced lifestyle.";
    }
}
