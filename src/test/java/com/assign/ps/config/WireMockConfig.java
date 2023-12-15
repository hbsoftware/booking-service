package com.assign.ps.config;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

@TestConfiguration
@ActiveProfiles("test")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class WireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockBooksService() {
        WireMock.configureFor("localhost", 7070);
        return new WireMockServer(options().port(7070));
    }

}
