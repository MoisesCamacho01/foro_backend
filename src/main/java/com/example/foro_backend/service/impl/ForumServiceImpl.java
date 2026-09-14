package com.example.foro_backend.service.impl;

import com.example.foro_backend.dto.forum.CreateCommentRequest;
import com.example.foro_backend.dto.forum.CreateQuestionRequest;
import com.example.foro_backend.dto.forum.ForumCommentResponse;
import com.example.foro_backend.dto.forum.VoteRequest;
import com.example.foro_backend.dto.forum.VoteStateResponse;
import com.example.foro_backend.exception.ResourceNotFoundException;
import com.example.foro_backend.mapper.CommentMapper;
import com.example.foro_backend.mapper.TreeBuilder;
import com.example.foro_backend.model.CommentModel;
import com.example.foro_backend.model.VoteModel;
import com.example.foro_backend.repository.CommentRepository;
import com.example.foro_backend.repository.VoteRepository;
import com.example.foro_backend.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForumServiceImpl implements ForumService {

    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;
    private final TreeBuilder treeBuilder;
    private final CommentMapper commentMapper;

    @Override
    public List<ForumCommentResponse> getAllQuestions(String currentUserAlias) {
        List<CommentModel> comments = commentRepository.findAll();
        List<VoteModel> userVotes = voteRepository.findByUserAlias(currentUserAlias);
        return treeBuilder.buildQuestionTree(comments, currentUserAlias, userVotes);
    }

    @Override
    public ForumCommentResponse createQuestion(String alias, CreateQuestionRequest request) {
        CommentModel question = new CommentModel(
                UUID.randomUUID().toString(),
                null,
                alias,
                request.body(),
                LocalDateTime.now(),
                "user",
                0,
                0
        );
        CommentModel saved = commentRepository.save(question);
        return commentMapper.toResponseWithoutChildren(saved, 0, alias, Map.of(saved.getId(), saved), Optional.empty());
    }

    @Override
    public ForumCommentResponse createReply(String alias, CreateCommentRequest request) {
        List<CommentModel> comments = commentRepository.findAll();
        Map<String, CommentModel> commentsById = comments.stream()
                .collect(Collectors.toMap(CommentModel::getId, comment -> comment));

        CommentModel parent = commentsById.get(request.parentId());
        if (parent == null) {
            throw new ResourceNotFoundException("Comentario padre no encontrado: " + request.parentId());
        }

        int level = treeBuilder.calculateLevel(parent.getId(), commentsById) + 1;
        CommentModel reply = new CommentModel(
                UUID.randomUUID().toString(),
                request.parentId(),
                alias,
                request.body(),
                LocalDateTime.now(),
                "user",
                0,
                0
        );
        CommentModel saved = commentRepository.save(reply);
        commentsById.put(saved.getId(), saved);
        return commentMapper.toResponseWithoutChildren(saved, level, alias, commentsById, Optional.empty());
    }

    @Override
    public synchronized VoteStateResponse toggleVote(String alias, String commentId, VoteRequest request) {
        CommentModel comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado: " + commentId));

        Optional<VoteModel> existingVote = voteRepository.findByCommentIdAndUserAlias(commentId, alias);
        String currentType = existingVote.map(VoteModel::getType).orElse(null);

        if ("like".equals(request.type())) {
            applyLikeToggle(comment, alias, currentType, existingVote.orElse(null));
        } else {
            applyDislikeToggle(comment, alias, currentType, existingVote.orElse(null));
        }

        CommentModel updated = commentRepository.update(comment);
        Optional<VoteModel> userVote = voteRepository.findByCommentIdAndUserAlias(commentId, alias);
        return buildVoteState(updated, userVote.orElse(null));
    }

    private void applyLikeToggle(CommentModel comment, String alias, String currentType, VoteModel existingVote) {
        if ("like".equals(currentType)) {
            comment.setLikesCount(Math.max(0, comment.getLikesCount() - 1));
            voteRepository.delete(existingVote);
            return;
        }

        if ("dislike".equals(currentType)) {
            comment.setDislikesCount(Math.max(0, comment.getDislikesCount() - 1));
            voteRepository.delete(existingVote);
        }

        comment.setLikesCount(comment.getLikesCount() + 1);
        voteRepository.save(new VoteModel(UUID.randomUUID().toString(), comment.getId(), alias, "like"));
    }

    private void applyDislikeToggle(CommentModel comment, String alias, String currentType, VoteModel existingVote) {
        if ("dislike".equals(currentType)) {
            comment.setDislikesCount(Math.max(0, comment.getDislikesCount() - 1));
            voteRepository.delete(existingVote);
            return;
        }

        if ("like".equals(currentType)) {
            comment.setLikesCount(Math.max(0, comment.getLikesCount() - 1));
            voteRepository.delete(existingVote);
        }

        comment.setDislikesCount(comment.getDislikesCount() + 1);
        voteRepository.save(new VoteModel(UUID.randomUUID().toString(), comment.getId(), alias, "dislike"));
    }

    private VoteStateResponse buildVoteState(CommentModel comment, VoteModel userVote) {
        boolean liked = userVote != null && "like".equals(userVote.getType());
        boolean disliked = userVote != null && "dislike".equals(userVote.getType());
        return new VoteStateResponse(liked, disliked, comment.getLikesCount(), comment.getDislikesCount());
    }
}
