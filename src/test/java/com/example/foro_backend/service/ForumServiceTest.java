package com.example.foro_backend.service;

import com.example.foro_backend.dto.forum.CreateCommentRequest;
import com.example.foro_backend.dto.forum.CreateQuestionRequest;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ForumServiceTest {

    @TempDir
    static Path tempDataDir;

    @DynamicPropertySource
    static void registerDataDir(DynamicPropertyRegistry registry) {
        registry.add("app.data.dir", () -> tempDataDir.toString());
    }

    @Autowired
    private ForumService forumService;

    @Test
    @Order(1)
    void getAllQuestionsBuildsNestedTreeFromSeed() {
        List<ForumCommentResponse> questions = forumService.getAllQuestions("test_user");

        assertThat(questions).hasSize(1);
        assertThat(questions.get(0).id()).isEqualTo("q1");
        assertThat(questions.get(0).children()).hasSize(1);
        assertThat(questions.get(0).children().get(0).id()).isEqualTo("r1");
        assertThat(questions.get(0).children().get(0).children().get(0).id()).isEqualTo("r1_1");
        assertThat(questions.get(0).votes().likes()).isEqualTo(15);
    }

    @Test
    @Order(2)
    void createQuestionPrependsUserQuestion() {
        ForumCommentResponse created = forumService.createQuestion(
                "moises_dev",
                new CreateQuestionRequest("¿Cómo estructurar el árbol?")
        );

        assertThat(created.level()).isZero();
        assertThat(created.badge()).isEqualTo("Nueva pregunta");
        assertThat(created.origin()).isEqualTo("user");
        assertThat(created.author().tone()).isEqualTo("current");

        List<ForumCommentResponse> questions = forumService.getAllQuestions("moises_dev");
        assertThat(questions.get(0).id()).isEqualTo(created.id());
    }

    @Test
    @Order(3)
    void createReplyCalculatesLevelAndBadge() {
        ForumCommentResponse directReply = forumService.createReply(
                "moises_dev",
                new CreateCommentRequest("q1", "Respuesta directa de prueba")
        );

        assertThat(directReply.level()).isEqualTo(1);
        assertThat(directReply.badge()).isEqualTo("Respuesta directa");

        ForumCommentResponse nestedReply = forumService.createReply(
                "moises_dev",
                new CreateCommentRequest(directReply.id(), "Sub-réplica de prueba")
        );

        assertThat(nestedReply.level()).isEqualTo(2);
        assertThat(nestedReply.badge()).isEqualTo("Sub-réplica escalonada");
    }
}
