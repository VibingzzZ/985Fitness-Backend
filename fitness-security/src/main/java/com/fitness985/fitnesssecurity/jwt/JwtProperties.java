package com.fitness985.fitnesssecurity.jwt;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String secret, Duration accessTokenExpiration, Duration refreshTokenExpiration) {
}
