package com.example.foro_backend.controller;

import com.example.foro_backend.dto.ApiResponse;
import com.example.foro_backend.dto.forum.CreateCommentRequest;
import com.example.foro_backend.dto.forum.ForumCommentResponse;
import com.example.foro_backend.dto.forum.VoteRequest;
import com.example.foro_backend.dto.forum.VoteStateResponse;
import com.example.foro_backend.security.AuthenticatedUserContext;
import com.example.foro_backend.service.ForumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CommentController {

    private final ForumService forumService;

    @PostMapping
    public ResponseEntity<ApiResponse<ForumCommentResponse>> createComment(
            @Valid @RequestBody CreateCommentRequest request
    ) {
        String alias = AuthenticatedUserContext.getAlias();
        ForumCommentResponse comment = forumService.createReply(alias, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Réplica publicada correctamente", comment));
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<ApiResponse<VoteStateResponse>> vote(
            @PathVariable String id,
            @Valid @RequestBody VoteRequest request
    ) {
        String alias = AuthenticatedUserContext.getAlias();
        VoteStateResponse voteState = forumService.toggleVote(alias, id, request);
        return ResponseEntity.ok(ApiResponse.success("Voto registrado correctamente", voteState));
    }
}
