package com.example.foro_backend.service;

import com.example.foro_backend.dto.forum.VoteRequest;
import com.example.foro_backend.dto.forum.VoteStateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VoteConcurrencyTest {

    @TempDir
    static Path tempDataDir;

    @DynamicPropertySource
    static void registerDataDir(DynamicPropertyRegistry registry) {
        registry.add("app.data.dir", () -> tempDataDir.toString());
    }

    @Autowired
    private ForumService forumService;

    @Test
    void concurrentLikesKeepConsistentCounts() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        try {
            List<Callable<VoteStateResponse>> tasks = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                String alias = "concurrent_user_" + i;
                tasks.add(() -> forumService.toggleVote(alias, "q1", new VoteRequest("like")));
            }

            List<Future<VoteStateResponse>> futures = executor.invokeAll(tasks);
            VoteStateResponse lastResult = futures.get(futures.size() - 1).get();

            assertThat(lastResult.likes()).isEqualTo(20);
            assertThat(lastResult.likes()).isGreaterThanOrEqualTo(0);
            assertThat(lastResult.dislikes()).isGreaterThanOrEqualTo(0);
        } finally {
            executor.shutdown();
        }
    }
}
