package com.strikezone.strikezone_backend.domain.comment.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private final Long commentId;
    private final Long postId;
    private final Long userId;
    private final String content;

    @Builder
    public CommentResponseDto(Long commentId, Long postId, Long userId, String content) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }
}
