package com.wellness.backend.integration.openfda;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class OpenFdaClient {

    private final WebClient webClient;

    public OpenFdaClient(WebClient webClient) {
        this.webClient = webClient;
    }


    public Mono<OpenFdaResponse> searchDrug(String keyword) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/drug/label.json")
                        .queryParam(
                                "search",
                                "openfda.generic_name:\"" + keyword.toLowerCase() + "\""
                        )
                        .queryParam("limit", 1)
                        .build()
                )
                .header(HttpHeaders.USER_AGENT, "Wellness-App/1.0")
                .retrieve()
                .bodyToMono(OpenFdaResponse.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.err.println("FDA API error: " + ex.getStatusCode());
                    return Mono.empty();
                })
                .onErrorResume(Exception.class, ex -> {
                    System.err.println("Unexpected FDA error: " + ex.getMessage());
                    return Mono.empty();
                });
    }
}
