package com.example.foro_backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentModel {

    private String id;
    private String parentId;
    private String authorAlias;
    private String body;
    private LocalDateTime createdAt;
    private String origin;
    private int likesCount;
    private int dislikesCount;
}
