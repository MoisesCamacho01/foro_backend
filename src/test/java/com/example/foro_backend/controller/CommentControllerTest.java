package com.example.foro_backend.controller;

import org.junit.jupiter.api.BeforeEach;
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

import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @TempDir
    static Path tempDataDir;

    @DynamicPropertySource
    static void registerDataDir(DynamicPropertyRegistry registry) {
        registry.add("app.data.dir", () -> tempDataDir.toString());
    }

    @Autowired
    private MockMvc mockMvc;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        token = loginAndGetToken("voter_user");
    }

    @Test
    void createCommentWithValidParentReturnsCreated() throws Exception {
        mockMvc.perform(post("/comments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "parentId": "q1",
                                  "body": "Totalmente de acuerdo, parentId es clave para el árbol."
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.parentId").value("q1"))
                .andExpect(jsonPath("$.data.level").value(1))
                .andExpect(jsonPath("$.data.badge").value("Respuesta directa"))
                .andExpect(jsonPath("$.data.origin").value("user"));
    }

    @Test
    void createCommentWithMissingParentReturnsNotFound() throws Exception {
        mockMvc.perform(post("/comments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "parentId": "missing-id",
                                  "body": "No debería publicarse"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result").value("ERROR"));
    }

    @Test
    void voteLikeIncrementsLikes() throws Exception {
        mockMvc.perform(post("/comments/q1/vote")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":"like"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.liked").value(true))
                .andExpect(jsonPath("$.data.disliked").value(false))
                .andExpect(jsonPath("$.data.likes").value(16))
                .andExpect(jsonPath("$.data.dislikes").value(1));
    }

    @Test
    void voteLikeToggleOffDecrementsLikes() throws Exception {
        mockMvc.perform(post("/comments/q1/vote")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":"like"}
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/comments/q1/vote")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":"like"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.liked").value(false))
                .andExpect(jsonPath("$.data.likes").value(15));
    }

    @Test
    void voteDislikeAfterLikeIsMutuallyExclusive() throws Exception {
        mockMvc.perform(post("/comments/q1/vote")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":"like"}
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/comments/q1/vote")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":"dislike"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.liked").value(false))
                .andExpect(jsonPath("$.data.disliked").value(true))
                .andExpect(jsonPath("$.data.likes").value(15))
                .andExpect(jsonPath("$.data.dislikes").value(2));
    }

    @Test
    void voteOnMissingCommentReturnsNotFound() throws Exception {
        mockMvc.perform(post("/comments/missing-id/vote")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":"like"}
                                """))
                .andExpect(status().isNotFound());
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
