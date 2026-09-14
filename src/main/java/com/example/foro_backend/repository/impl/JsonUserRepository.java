package com.example.foro_backend.repository.impl;

import com.example.foro_backend.config.AppProperties;
import com.example.foro_backend.model.UserModel;
import com.example.foro_backend.repository.UserRepository;
import tools.jackson.core.type.TypeReference;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Repository
public class JsonUserRepository implements UserRepository {

    private static final TypeReference<List<UserModel>> USER_LIST_TYPE = new TypeReference<>() {};

    private final JsonFileStore fileStore;
    private final Path usersFile;

    public JsonUserRepository(JsonFileStore fileStore, AppProperties appProperties) {
        this.fileStore = fileStore;
        this.usersFile = Path.of(appProperties.data().dir(), "users.json");
    }

    @PostConstruct
    void init() {
        fileStore.ensureFileExists(usersFile);
    }

    @Override
    public Optional<UserModel> findByAlias(String alias) {
        String normalized = normalizeAlias(alias);
        return fileStore.readList(usersFile, USER_LIST_TYPE).stream()
                .filter(user -> user.getAlias().equals(normalized))
                .findFirst();
    }

    @Override
    public boolean existsByAlias(String alias) {
        return findByAlias(alias).isPresent();
    }

    @Override
    public UserModel save(UserModel user) {
        user.setAlias(normalizeAlias(user.getAlias()));
        return fileStore.readModifyWrite(usersFile, USER_LIST_TYPE, users -> {
            users.add(user);
            return user;
        });
    }

    private String normalizeAlias(String alias) {
        return alias.trim().toLowerCase();
    }
}
