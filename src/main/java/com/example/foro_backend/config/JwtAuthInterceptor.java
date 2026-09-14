package com.example.foro_backend.config;

import com.example.foro_backend.exception.UnauthorizedException;
import com.example.foro_backend.security.AuthenticatedUserContext;
import com.example.foro_backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public JwtAuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token de autenticación requerido");
        }

        String token = authorization.substring(7).trim();
        if (!jwtService.isTokenValid(token)) {
            throw new UnauthorizedException("Token inválido o expirado");
        }

        AuthenticatedUserContext.setAlias(jwtService.extractAlias(token));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        AuthenticatedUserContext.clear();
    }
}
