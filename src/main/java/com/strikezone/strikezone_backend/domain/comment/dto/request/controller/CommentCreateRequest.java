package com.strikezone.strikezone_backend.domain.comment.dto.request.controller;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private Long postId;
    private Long userId;
    private String content;

    @Builder
    public CommentCreateRequest(Long postId, Long userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }
}
