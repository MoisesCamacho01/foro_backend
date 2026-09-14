package com.example.foro_backend.dto.auth;

public record LoginResponse(
        String alias,
        String token
) {
}
