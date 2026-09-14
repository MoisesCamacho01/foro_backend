package com.example.foro_backend.config;

import com.example.foro_backend.model.CommentModel;
import com.example.foro_backend.repository.CommentRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final TypeReference<List<CommentModel>> COMMENT_LIST_TYPE = new TypeReference<>() {};

    private final CommentRepository commentRepository;
    private final ObjectMapper objectMapper;

    public DataInitializer(CommentRepository commentRepository, ObjectMapper objectMapper) {
        this.commentRepository = commentRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        if (!commentRepository.findAll().isEmpty()) {
            return;
        }

        ClassPathResource seedResource = new ClassPathResource("seed/comments.json");
        try (InputStream inputStream = seedResource.getInputStream()) {
            List<CommentModel> seedComments = objectMapper.readValue(inputStream, COMMENT_LIST_TYPE);
            seedComments.forEach(commentRepository::save);
        }
    }
}
