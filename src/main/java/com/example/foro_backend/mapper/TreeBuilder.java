package com.example.foro_backend.mapper;

import com.example.foro_backend.dto.forum.ForumCommentResponse;
import com.example.foro_backend.model.CommentModel;
import com.example.foro_backend.model.VoteModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TreeBuilder {

    private final CommentMapper commentMapper;

    public TreeBuilder(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public List<ForumCommentResponse> buildQuestionTree(
            List<CommentModel> comments,
            String currentUserAlias,
            List<VoteModel> userVotes
    ) {
        Map<String, CommentModel> commentsById = comments.stream()
                .collect(Collectors.toMap(CommentModel::getId, comment -> comment));

        Map<String, List<CommentModel>> childrenByParentId = new HashMap<>();
        for (CommentModel comment : comments) {
            if (comment.getParentId() != null && commentsById.containsKey(comment.getParentId())) {
                childrenByParentId.computeIfAbsent(comment.getParentId(), key -> new ArrayList<>()).add(comment);
            }
        }

        childrenByParentId.values().forEach(childList ->
                childList.sort(Comparator.comparing(CommentModel::getCreatedAt)));

        Map<String, VoteModel> votesByCommentId = userVotes.stream()
                .collect(Collectors.toMap(VoteModel::getCommentId, vote -> vote, (first, second) -> second));

        return comments.stream()
                .filter(comment -> comment.getParentId() == null)
                .sorted(rootComparator())
                .map(root -> commentMapper.toResponse(
                        root,
                        0,
                        currentUserAlias,
                        commentsById,
                        childrenByParentId,
                        votesByCommentId
                ))
                .toList();
    }

    public int calculateLevel(String commentId, Map<String, CommentModel> commentsById) {
        CommentModel comment = commentsById.get(commentId);
        if (comment == null) {
            return 0;
        }
        if (comment.getParentId() == null) {
            return 0;
        }
        return 1 + calculateLevel(comment.getParentId(), commentsById);
    }

    private Comparator<CommentModel> rootComparator() {
        return Comparator
                .comparing((CommentModel comment) -> !"user".equals(comment.getOrigin()))
                .thenComparing(CommentModel::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()));
    }
}
