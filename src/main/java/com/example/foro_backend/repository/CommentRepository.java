package com.example.foro_backend.repository;

import com.example.foro_backend.model.CommentModel;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<CommentModel> findAll();

    Optional<CommentModel> findById(String id);

    CommentModel save(CommentModel comment);

    CommentModel update(CommentModel comment);
}
