package com.example.foro_backend.dto.forum;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ForumUserResponse(
        String displayName,
        String handle,
        String initials,
        String tone
) {
}
