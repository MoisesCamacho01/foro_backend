package com.example.foro_backend.repository;

import com.example.foro_backend.model.VoteModel;

import java.util.List;
import java.util.Optional;

public interface VoteRepository {

    List<VoteModel> findByUserAlias(String userAlias);

    Optional<VoteModel> findByCommentIdAndUserAlias(String commentId, String userAlias);

    VoteModel save(VoteModel vote);

    void delete(VoteModel vote);
}
