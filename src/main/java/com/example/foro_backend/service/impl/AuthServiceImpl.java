package com.example.foro_backend.service.impl;

import com.example.foro_backend.dto.auth.LoginRequest;
import com.example.foro_backend.dto.auth.LoginResponse;
import com.example.foro_backend.dto.auth.LogoutResponse;
import com.example.foro_backend.model.UserModel;
import com.example.foro_backend.repository.UserRepository;
import com.example.foro_backend.service.AuthService;
import com.example.foro_backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {
        String alias = normalizeAlias(request.alias());
        UserModel user = userRepository.findByAlias(alias)
                .orElseGet(() -> createUser(alias));

        String token = jwtService.generateToken(user.getAlias());
        return new LoginResponse(user.getAlias(), token);
    }

    private UserModel createUser(String alias) {
        UserModel user = new UserModel(
                UUID.randomUUID().toString(),
                alias,
                LocalDateTime.now()
        );
        return userRepository.save(user);
    }

    @Override
    public LogoutResponse logout() {
        // Stateless: el cliente descarta el token; no hay blacklist en servidor.
        return new LogoutResponse("Sesión cerrada");
    }

    private String normalizeAlias(String alias) {
        return alias.trim().toLowerCase();
    }
}
