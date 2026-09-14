package com.example.foro_backend.dto.forum;

import jakarta.validation.constraints.NotBlank;

public record VoteRequest(
        @NotBlank(message = "El tipo de voto es obligatorio")
        String type
) {
    public VoteRequest {
        if (type != null) {
            type = type.trim().toLowerCase();
        }
        if (type != null && !type.equals("like") && !type.equals("dislike")) {
            throw new IllegalArgumentException("type debe ser 'like' o 'dislike'");
        }
    }
}
