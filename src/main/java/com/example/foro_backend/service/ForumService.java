package com.example.foro_backend.service;

import com.example.foro_backend.dto.forum.CreateCommentRequest;
import com.example.foro_backend.dto.forum.CreateQuestionRequest;
import com.example.foro_backend.dto.forum.ForumCommentResponse;
import com.example.foro_backend.dto.forum.VoteRequest;
import com.example.foro_backend.dto.forum.VoteStateResponse;

import java.util.List;

public interface ForumService {

    List<ForumCommentResponse> getAllQuestions(String currentUserAlias);

    ForumCommentResponse createQuestion(String alias, CreateQuestionRequest request);

    ForumCommentResponse createReply(String alias, CreateCommentRequest request);

    VoteStateResponse toggleVote(String alias, String commentId, VoteRequest request);
}
