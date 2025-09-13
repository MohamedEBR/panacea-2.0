package com.example.panacea.config;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityConfigurationTest {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfigurationTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testSecurityHeaders() {
        String url = "http://localhost:" + port + "/api/v1/auth/test";
        logger.info("Testing URL: {}", url);

        ResponseEntity<String> response = testRestTemplate.getForEntity(url, String.class);
        logger.info("Response status: {}", response.getStatusCode());

        HttpHeaders headers = response.getHeaders();
        logger.info("Headers received: {}", headers);

        assertThat(headers.get("Content-Security-Policy")).isNotNull()
            .contains("default-src 'self'; script-src 'self'; object-src 'none'; style-src 'self' 'unsafe-inline'; font-src 'self'; img-src 'self' data:; frame-ancestors 'none';");
        assertThat(headers.get("X-Frame-Options")).isNotNull()
            .contains("DENY");
        assertThat(headers.get("Strict-Transport-Security")).isNotNull()
            .contains("max-age=31536000; includeSubDomains");
        assertThat(headers.get("X-Content-Type-Options")).isNotNull()
            .contains("nosniff");
        assertThat(headers.get("X-XSS-Protection")).isNotNull()
            .contains("1; mode=block");
        assertThat(headers.get("Referrer-Policy")).isNotNull()
            .contains("strict-origin-when-cross-origin");
        assertThat(headers.get("Permissions-Policy")).isNotNull()
            .contains("geolocation=(), microphone=(), camera=(), payment=(), usb=()");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}