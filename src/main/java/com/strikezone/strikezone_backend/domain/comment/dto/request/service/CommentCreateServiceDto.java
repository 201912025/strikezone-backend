package com.strikezone.strikezone_backend.domain.comment.dto.request.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentCreateServiceDto {
    private final Long postId;
    private final Long userId;
    private final String content;

    @Builder
    public CommentCreateServiceDto(Long postId, Long userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }
}
