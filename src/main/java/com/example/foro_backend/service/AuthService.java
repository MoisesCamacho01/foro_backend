package com.example.foro_backend.service;

import com.example.foro_backend.dto.auth.LoginRequest;
import com.example.foro_backend.dto.auth.LoginResponse;
import com.example.foro_backend.dto.auth.LogoutResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    LogoutResponse logout();
}
