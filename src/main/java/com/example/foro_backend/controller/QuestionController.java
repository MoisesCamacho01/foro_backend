package com.example.foro_backend.controller;

import com.example.foro_backend.dto.ApiResponse;
import com.example.foro_backend.dto.forum.CreateQuestionRequest;
import com.example.foro_backend.dto.forum.ForumCommentResponse;
import com.example.foro_backend.security.AuthenticatedUserContext;
import com.example.foro_backend.service.ForumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/questions")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class QuestionController {

    private final ForumService forumService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ForumCommentResponse>>> getQuestions() {
        String alias = AuthenticatedUserContext.getAlias();
        List<ForumCommentResponse> questions = forumService.getAllQuestions(alias);
        return ResponseEntity.ok(ApiResponse.success("Preguntas obtenidas correctamente", questions));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ForumCommentResponse>> createQuestion(
            @Valid @RequestBody CreateQuestionRequest request
    ) {
        String alias = AuthenticatedUserContext.getAlias();
        ForumCommentResponse question = forumService.createQuestion(alias, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pregunta publicada correctamente", question));
    }
}
