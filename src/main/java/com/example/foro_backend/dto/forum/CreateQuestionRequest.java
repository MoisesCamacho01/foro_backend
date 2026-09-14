package com.example.foro_backend.dto.forum;

import jakarta.validation.constraints.NotBlank;

public record CreateQuestionRequest(
        @NotBlank(message = "El contenido de la pregunta es obligatorio")
        String body
) {
    public CreateQuestionRequest {
        body = body != null ? body.trim() : null;
    }
}
