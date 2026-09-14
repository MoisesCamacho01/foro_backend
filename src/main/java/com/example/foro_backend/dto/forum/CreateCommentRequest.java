package com.example.foro_backend.dto.forum;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(
        @NotBlank(message = "El parentId es obligatorio")
        String parentId,
        @NotBlank(message = "El contenido de la réplica es obligatorio")
        String body
) {
    public CreateCommentRequest {
        parentId = parentId != null ? parentId.trim() : null;
        body = body != null ? body.trim() : null;
    }
}
