package com.example.foro_backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
        DataProperties data,
        JwtProperties jwt
) {

    public record DataProperties(String dir) {}

    public record JwtProperties(String secret, long expirationMs) {}
}
