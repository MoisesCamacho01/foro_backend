package com.example.foro_backend.service;

import com.example.foro_backend.dto.forum.CreateCommentRequest;
import com.example.foro_backend.dto.forum.ForumCommentResponse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ForumServiceReplyLimitTest {

    @TempDir
    static Path tempDataDir;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("app.data.dir", () -> tempDataDir.toString());
        registry.add("app.forum.max-reply-levels", () -> "3");
    }

    @Autowired
    private ForumService forumService;

    @Test
    @Order(1)
    void createReplyAllowsLevelsUpToConfiguredLimit() {
        ForumCommentResponse level1 = forumService.createReply(
                "limit_user",
                new CreateCommentRequest("q1", "Respuesta nivel 1")
        );
        ForumCommentResponse level2 = forumService.createReply(
                "limit_user",
                new CreateCommentRequest(level1.id(), "Respuesta nivel 2")
        );
        ForumCommentResponse level3 = forumService.createReply(
                "limit_user",
                new CreateCommentRequest(level2.id(), "Respuesta nivel 3")
        );

        assertThat(level1.level()).isEqualTo(1);
        assertThat(level2.level()).isEqualTo(2);
        assertThat(level3.level()).isEqualTo(3);
    }

    @Test
    @Order(2)
    void createReplyRejectsLevelBeyondConfiguredLimit() {
        ForumCommentResponse level1 = forumService.createReply(
                "limit_user",
                new CreateCommentRequest("q1", "Otra respuesta nivel 1")
        );
        ForumCommentResponse level2 = forumService.createReply(
                "limit_user",
                new CreateCommentRequest(level1.id(), "Otra respuesta nivel 2")
        );
        ForumCommentResponse level3 = forumService.createReply(
                "limit_user",
                new CreateCommentRequest(level2.id(), "Otra respuesta nivel 3")
        );

        assertThatThrownBy(() -> forumService.createReply(
                "limit_user",
                new CreateCommentRequest(level3.id(), "Respuesta nivel 4")
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("máximo de 3 niveles");
    }
}
