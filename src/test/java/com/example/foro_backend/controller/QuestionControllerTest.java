package com.example.foro_backend.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuestionControllerTest {

    @TempDir
    static Path tempDataDir;

    @DynamicPropertySource
    static void registerDataDir(DynamicPropertyRegistry registry) {
        registry.add("app.data.dir", () -> tempDataDir.toString());
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void getQuestionsWithoutTokenReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/questions"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.result").value("ERROR"));
    }

    @Test
    @Order(2)
    void getQuestionsWithValidTokenReturnsSeedTree() throws Exception {
        String token = loginAndGetToken("forum_reader");

        mockMvc.perform(get("/questions")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data[0].id").value("q1"))
                .andExpect(jsonPath("$.data[0].children[0].id").value("r1"))
                .andExpect(jsonPath("$.data[0].children[0].children[0].id").value("r1_1"))
                .andExpect(jsonPath("$.data[0].children[0].children[0].children[0].id").value("r1_1_1"));
    }

    @Test
    @Order(3)
    void createQuestionReturnsCreatedResponse() throws Exception {
        String token = loginAndGetToken("question_author");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/questions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"body":"¿Cómo estructurar el estado del árbol de comentarios?"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.level").value(0))
                .andExpect(jsonPath("$.data.badge").value("Nueva pregunta"))
                .andExpect(jsonPath("$.data.origin").value("user"))
                .andExpect(jsonPath("$.data.author.tone").value("current"));
    }

    @Test
    @Order(4)
    void createQuestionWithEmptyBodyReturnsBadRequest() throws Exception {
        String token = loginAndGetToken("invalid_author");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/questions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"body":""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value("ERROR"));
    }

    @Test
    @Order(5)
    void loginRemainsPublicWithoutToken() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"alias":"public_user"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").exists());
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
