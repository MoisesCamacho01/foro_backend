package com.example.foro_backend.repository;

import com.example.foro_backend.model.UserModel;

import java.util.Optional;

public interface UserRepository {

    Optional<UserModel> findByAlias(String alias);

    boolean existsByAlias(String alias);

    UserModel save(UserModel user);
}
