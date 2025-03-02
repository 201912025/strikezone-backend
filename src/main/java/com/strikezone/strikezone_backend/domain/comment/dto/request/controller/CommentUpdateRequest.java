package com.strikezone.strikezone_backend.domain.comment.dto.request.controller;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {
    private Long commentId;
    private Long userId;
    private String content;

    @Builder
    public CommentUpdateRequest(Long commentId, Long userId, String content) {
        this.commentId = commentId;
        this.userId = userId;
        this.content = content;
    }
}
