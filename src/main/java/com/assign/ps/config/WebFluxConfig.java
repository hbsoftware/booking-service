package com.assign.ps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {

    @Bean
    public WebClient getWebClient() {

        final ConnectionProvider provider = ConnectionProvider.builder("fixed")
                .maxConnections(1000)
                .pendingAcquireTimeout(Duration.ofSeconds(10))
                .maxIdleTime(Duration.ofSeconds(10))
                .maxLifeTime(Duration.ofSeconds(10))
                .evictInBackground(Duration.ofSeconds(5))
                .metrics(true)
                .build();

        final HttpClient httpClient = HttpClient.create(provider);

        final ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient.wiretap(true));
        final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build();
        return WebClient.builder()
                .clientConnector(connector)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(exchangeStrategies)
                .build();
    }
}
