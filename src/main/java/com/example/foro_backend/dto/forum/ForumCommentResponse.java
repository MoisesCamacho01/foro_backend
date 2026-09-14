package com.example.foro_backend.dto.forum;

import java.util.List;

public record ForumCommentResponse(
        String id,
        String parentId,
        ForumUserResponse author,
        String body,
        String createdLabel,
        String relativeLabel,
        int level,
        String badge,
        String origin,
        VoteStateResponse votes,
        List<ForumCommentResponse> children
) {
}
