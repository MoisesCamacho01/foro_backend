package com.example.foro_backend.repository.impl;

import com.example.foro_backend.config.AppProperties;
import com.example.foro_backend.exception.ResourceNotFoundException;
import com.example.foro_backend.model.CommentModel;
import com.example.foro_backend.repository.CommentRepository;
import tools.jackson.core.type.TypeReference;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Repository
public class JsonCommentRepository implements CommentRepository {

    private static final TypeReference<List<CommentModel>> COMMENT_LIST_TYPE = new TypeReference<>() {};

    private final JsonFileStore fileStore;
    private final Path commentsFile;

    public JsonCommentRepository(JsonFileStore fileStore, AppProperties appProperties) {
        this.fileStore = fileStore;
        this.commentsFile = Path.of(appProperties.data().dir(), "comments.json");
    }

    @PostConstruct
    void init() {
        fileStore.ensureFileExists(commentsFile);
    }

    @Override
    public List<CommentModel> findAll() {
        return fileStore.readList(commentsFile, COMMENT_LIST_TYPE);
    }

    @Override
    public Optional<CommentModel> findById(String id) {
        return findAll().stream()
                .filter(comment -> comment.getId().equals(id))
                .findFirst();
    }

    @Override
    public CommentModel save(CommentModel comment) {
        return fileStore.readModifyWrite(commentsFile, COMMENT_LIST_TYPE, comments -> {
            comments.add(comment);
            return comment;
        });
    }

    @Override
    public CommentModel update(CommentModel comment) {
        return fileStore.readModifyWrite(commentsFile, COMMENT_LIST_TYPE, comments -> {
            for (int i = 0; i < comments.size(); i++) {
                if (comments.get(i).getId().equals(comment.getId())) {
                    comments.set(i, comment);
                    return comment;
                }
            }
            throw new ResourceNotFoundException("Comentario no encontrado: " + comment.getId());
        });
    }
}
