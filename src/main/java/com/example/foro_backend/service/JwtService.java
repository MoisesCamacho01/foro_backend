package com.example.foro_backend.service;

import com.example.foro_backend.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String ALIAS_CLAIM = "alias";

    private final AppProperties appProperties;
    private final SecretKey signKey;

    public JwtService(AppProperties appProperties) {
        this.appProperties = appProperties;
        this.signKey = Keys.hmacShaKeyFor(appProperties.jwt().secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String alias) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + appProperties.jwt().expirationMs());

        return Jwts.builder()
                .subject(alias)
                .claim(ALIAS_CLAIM, alias)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signKey)
                .compact();
    }

    public String extractAlias(String token) {
        return parseClaims(token).get(ALIAS_CLAIM, String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
