package com.example.foro_backend.repository.impl;

import com.example.foro_backend.config.AppProperties;
import com.example.foro_backend.model.VoteModel;
import com.example.foro_backend.repository.VoteRepository;
import tools.jackson.core.type.TypeReference;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Repository
public class JsonVoteRepository implements VoteRepository {

    private static final TypeReference<List<VoteModel>> VOTE_LIST_TYPE = new TypeReference<>() {};

    private final JsonFileStore fileStore;
    private final Path votesFile;

    public JsonVoteRepository(JsonFileStore fileStore, AppProperties appProperties) {
        this.fileStore = fileStore;
        this.votesFile = Path.of(appProperties.data().dir(), "votes.json");
    }

    @PostConstruct
    void init() {
        fileStore.ensureFileExists(votesFile);
    }

    @Override
    public List<VoteModel> findByUserAlias(String userAlias) {
        String normalizedAlias = userAlias.trim().toLowerCase();
        return fileStore.readList(votesFile, VOTE_LIST_TYPE).stream()
                .filter(vote -> vote.getUserAlias().equals(normalizedAlias))
                .toList();
    }

    @Override
    public Optional<VoteModel> findByCommentIdAndUserAlias(String commentId, String userAlias) {
        String normalizedAlias = userAlias.trim().toLowerCase();
        return fileStore.readList(votesFile, VOTE_LIST_TYPE).stream()
                .filter(vote -> vote.getCommentId().equals(commentId)
                        && vote.getUserAlias().equals(normalizedAlias))
                .findFirst();
    }

    @Override
    public VoteModel save(VoteModel vote) {
        vote.setUserAlias(vote.getUserAlias().trim().toLowerCase());
        return fileStore.readModifyWrite(votesFile, VOTE_LIST_TYPE, votes -> {
            votes.removeIf(existing -> existing.getCommentId().equals(vote.getCommentId())
                    && existing.getUserAlias().equals(vote.getUserAlias()));
            votes.add(vote);
            return vote;
        });
    }

    @Override
    public void delete(VoteModel vote) {
        fileStore.readModifyWrite(votesFile, VOTE_LIST_TYPE, votes -> {
            Iterator<VoteModel> iterator = votes.iterator();
            while (iterator.hasNext()) {
                VoteModel existing = iterator.next();
                if (existing.getId().equals(vote.getId())) {
                    iterator.remove();
                    break;
                }
            }
            return null;
        });
    }
}
