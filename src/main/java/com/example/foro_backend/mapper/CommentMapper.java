package com.example.foro_backend.mapper;

import com.example.foro_backend.dto.forum.ForumCommentResponse;
import com.example.foro_backend.dto.forum.ForumUserResponse;
import com.example.foro_backend.dto.forum.VoteStateResponse;
import com.example.foro_backend.model.CommentModel;
import com.example.foro_backend.model.VoteModel;
import com.example.foro_backend.util.DateLabelHelper;
import com.example.foro_backend.util.UserPresentationHelper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CommentMapper {

    public ForumCommentResponse toResponse(
            CommentModel comment,
            int level,
            String currentUserAlias,
            Map<String, CommentModel> commentsById,
            Map<String, List<CommentModel>> childrenByParentId,
            Map<String, VoteModel> votesByCommentId
    ) {
        List<CommentModel> children = childrenByParentId.getOrDefault(comment.getId(), Collections.emptyList());
        List<ForumCommentResponse> childResponses = children.stream()
                .map(child -> toResponse(
                        child,
                        level + 1,
                        currentUserAlias,
                        commentsById,
                        childrenByParentId,
                        votesByCommentId
                ))
                .toList();

        VoteStateResponse votes = buildVoteState(comment, votesByCommentId.get(comment.getId()));
        CommentModel parent = comment.getParentId() != null ? commentsById.get(comment.getParentId()) : null;

        return new ForumCommentResponse(
                comment.getId(),
                comment.getParentId(),
                UserPresentationHelper.buildForumUser(comment.getAuthorAlias(), level, currentUserAlias),
                comment.getBody(),
                DateLabelHelper.formatCreatedLabel(comment.getCreatedAt()),
                DateLabelHelper.formatRelativeLabel(comment.getCreatedAt()),
                level,
                resolveBadge(comment, level, parent),
                comment.getOrigin(),
                votes,
                childResponses
        );
    }

    public ForumCommentResponse toResponseWithoutChildren(
            CommentModel comment,
            int level,
            String currentUserAlias,
            Map<String, CommentModel> commentsById,
            Optional<VoteModel> userVote
    ) {
        CommentModel parent = comment.getParentId() != null ? commentsById.get(comment.getParentId()) : null;
        VoteStateResponse votes = buildVoteState(comment, userVote.orElse(null));

        return new ForumCommentResponse(
                comment.getId(),
                comment.getParentId(),
                UserPresentationHelper.buildForumUser(comment.getAuthorAlias(), level, currentUserAlias),
                comment.getBody(),
                DateLabelHelper.formatCreatedLabel(comment.getCreatedAt()),
                DateLabelHelper.formatRelativeLabel(comment.getCreatedAt()),
                level,
                resolveBadge(comment, level, parent),
                comment.getOrigin(),
                votes,
                List.of()
        );
    }

    private VoteStateResponse buildVoteState(CommentModel comment, VoteModel userVote) {
        boolean liked = userVote != null && "like".equals(userVote.getType());
        boolean disliked = userVote != null && "dislike".equals(userVote.getType());
        return new VoteStateResponse(liked, disliked, comment.getLikesCount(), comment.getDislikesCount());
    }

    private String resolveBadge(CommentModel comment, int level, CommentModel parent) {
        if ("user".equals(comment.getOrigin())) {
            if (level == 0) {
                return "Nueva pregunta";
            }
            if (parent != null && parent.getParentId() == null) {
                return "Respuesta directa";
            }
            return "Sub-réplica escalonada";
        }

        if (level == 0) {
            return "";
        }
        return "Nivel " + level;
    }
}
