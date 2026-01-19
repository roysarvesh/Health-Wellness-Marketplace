package com.wellness.backend.integration.who;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WhoHealthService {

    private final RestTemplate restTemplate;

    public WhoHealthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getDiseaseInfo(String keyword) {

        String url = UriComponentsBuilder
                .fromUriString("https://ghoapi.azureedge.net/api/DIMENSION/COUNTRY")
                .build()
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }
}
