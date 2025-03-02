package com.strikezone.strikezone_backend.domain.comment.dto.request.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentUpdateServiceDto {
    private final Long commentId;
    private final Long userId;
    private final String content;

    @Builder
    public CommentUpdateServiceDto(Long commentId, Long userId, String content) {
        this.commentId = commentId;
        this.userId = userId;
        this.content = content;
    }
}
