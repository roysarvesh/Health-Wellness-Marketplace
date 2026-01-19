package com.wellness.backend.controller;

import com.wellness.backend.integration.openfda.OpenFdaService;
import com.wellness.backend.integration.who.WhoApiClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/health-data")
public class HealthDataController {

    private final OpenFdaService openFdaService;
    private final WhoApiClient whoApiClient;

    public HealthDataController(
            OpenFdaService openFdaService,
            WhoApiClient whoApiClient
    ) {
        this.openFdaService = openFdaService;
        this.whoApiClient = whoApiClient;
    }


    @GetMapping("/fda/warnings")
    public Mono<ResponseEntity<String>> drugWarnings(
            @RequestParam String drug
    ) {
        return openFdaService
                .getDrugWarnings(drug)
                .map(ResponseEntity::ok);
    }


    @GetMapping("/fda/events")
    public Mono<ResponseEntity<String>> drugEvents(
            @RequestParam String query
    ) {
        return openFdaService
                .searchDrugEvents(query)
                .map(ResponseEntity::ok);
    }


    @GetMapping("/who")
    public ResponseEntity<String> whoGuidelines(
            @RequestParam String topic
    ) {
        return ResponseEntity.ok(
                whoApiClient.getHealthGuidelines(topic)
        );
    }
}
