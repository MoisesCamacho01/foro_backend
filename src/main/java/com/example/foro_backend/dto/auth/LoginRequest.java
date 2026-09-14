package com.example.foro_backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "El alias es obligatorio")
        @Size(min = 1, max = 50, message = "El alias debe tener entre 1 y 50 caracteres")
        String alias
) {
    public LoginRequest {
        alias = alias != null ? alias.trim() : null;
    }
}
