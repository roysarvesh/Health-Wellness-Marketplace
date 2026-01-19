package com.wellness.backend.integration.openfda;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class OpenFdaService {

    private final WebClient webClient;

    public OpenFdaService(WebClient webClient) {
        this.webClient = webClient;
    }

    // ================================
    // FDA DRUG WARNINGS
    // ================================
    public Mono<String> getDrugWarnings(String drugName) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/drug/label.json")
                        .queryParam(
                                "search",
                                "openfda.generic_name:\"" + drugName.toLowerCase() + "\""
                        )
                        .queryParam("limit", 1)
                        .build()
                )
                .header(HttpHeaders.USER_AGENT, "Wellness-App/1.0")
                .retrieve()
                .onStatus(
                        HttpStatus.FORBIDDEN::equals,
                        response -> Mono.error(
                                new RuntimeException("FDA API access forbidden (403). Try again later.")
                        )
                )
                .onStatus(
                        HttpStatus.NOT_FOUND::equals,
                        response -> Mono.error(
                                new RuntimeException("No FDA data found for this drug.")
                        )
                )
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class, ex ->
                        Mono.just("""
                            {
                              "error": "FDA service error",
                              "status": %d,
                              "message": "%s"
                            }
                            """.formatted(ex.getStatusCode().value(), ex.getMessage()))
                )
                .onErrorResume(Exception.class, ex ->
                        Mono.just("""
                            {
                              "error": "Unexpected error",
                              "message": "%s"
                            }
                            """.formatted(ex.getMessage()))
                );
    }

    // ================================
    // FDA DRUG EVENTS
    // ================================
    public Mono<String> searchDrugEvents(String searchQuery) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/drug/event.json")
                        .queryParam("search", searchQuery)
                        .queryParam("limit", 5)
                        .build()
                )
                .header(HttpHeaders.USER_AGENT, "Wellness-App/1.0")
                .retrieve()
                .onStatus(
                        HttpStatus.FORBIDDEN::equals,
                        response -> Mono.error(
                                new RuntimeException("FDA API access forbidden (403).")
                        )
                )
                .bodyToMono(String.class)
                .onErrorResume(Exception.class, ex ->
                        Mono.just("""
                            {
                              "error": "FDA event lookup failed",
                              "message": "%s"
                            }
                            """.formatted(ex.getMessage()))
                );
    }
}
