package com.example.foro_backend.dto.forum;

public record VoteStateResponse(
        boolean liked,
        boolean disliked,
        int likes,
        int dislikes
) {
}
