package com.wellness.backend.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${openfda.base-url:https://api.fda.gov}")
    private String openFdaBaseUrl;

    @Bean
    public WebClient webClient() {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofSeconds(5))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS))
                );

        return WebClient.builder()
                .baseUrl(openFdaBaseUrl)

                // ✅ REQUIRED FOR OPENFDA (403 FIX)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "Wellness-Backend/1.0")

                // ✅ HANDLE LARGE JSON PAYLOADS
                .exchangeStrategies(
                        ExchangeStrategies.builder()
                                .codecs(config ->
                                        config.defaultCodecs()
                                                .maxInMemorySize(10 * 1024 * 1024)
                                )
                                .build()
                )

                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
