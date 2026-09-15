package com.example.foro_backend.controller;

import com.example.foro_backend.dto.ApiResponse;
import com.example.foro_backend.dto.forum.ForumConfigResponse;
import com.example.foro_backend.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ConfigController {

    private final ForumService forumService;

    @GetMapping
    public ResponseEntity<ApiResponse<ForumConfigResponse>> getForumConfig() {
        ForumConfigResponse config = forumService.getForumConfig();
        return ResponseEntity.ok(ApiResponse.success("Configuración del foro obtenida correctamente", config));
    }
}
