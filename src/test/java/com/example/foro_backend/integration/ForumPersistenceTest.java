package com.example.foro_backend.integration;

import com.example.foro_backend.model.CommentModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ForumPersistenceTest {

    @TempDir
    static Path tempDataDir;

    @DynamicPropertySource
    static void registerDataDir(DynamicPropertyRegistry registry) {
        registry.add("app.data.dir", () -> tempDataDir.toString());
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createQuestionPersistsToCommentsJson() throws Exception {
        String token = loginAndGetToken("persist_user");

        mockMvc.perform(post("/questions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"body":"Pregunta persistida en JSON"}
                                """))
                .andExpect(status().isCreated());

        Path commentsFile = tempDataDir.resolve("comments.json");
        assertThat(Files.exists(commentsFile)).isTrue();

        List<CommentModel> comments = objectMapper.readValue(
                Files.readAllBytes(commentsFile),
                new TypeReference<>() {}
        );

        assertThat(comments)
                .anyMatch(comment ->
                        "persist_user".equals(comment.getAuthorAlias())
                                && "Pregunta persistida en JSON".equals(comment.getBody())
                                && "user".equals(comment.getOrigin())
                );
    }

    private String loginAndGetToken(String alias) throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"alias":"%s"}
                                """.formatted(alias)))
                .andExpect(status().isOk())
                .andReturn();

        return com.jayway.jsonpath.JsonPath.read(
                loginResult.getResponse().getContentAsString(),
                "$.data.token"
        );
    }
}
