package com.example.foro_backend.controller;

import com.example.foro_backend.dto.ApiResponse;
import com.example.foro_backend.dto.auth.LoginRequest;
import com.example.foro_backend.dto.auth.LoginResponse;
import com.example.foro_backend.dto.auth.LogoutResponse;
import com.example.foro_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Inicio de sesión exitoso", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout() {
        LogoutResponse response = authService.logout();
        return ResponseEntity.ok(ApiResponse.success(response.message(), response));
    }
}
