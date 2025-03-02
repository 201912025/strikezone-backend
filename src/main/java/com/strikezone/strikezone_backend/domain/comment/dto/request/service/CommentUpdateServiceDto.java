package com.strikezone.strikezone_backend.domain.comment.dto.request.service;

import com.strikezone.strikezone_backend.domain.comment.dto.request.controller.CommentUpdateRequest;
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

    public static CommentUpdateServiceDto from(CommentUpdateRequest commentUpdateRequest, Long commentId) {
        return CommentUpdateServiceDto.builder()
                                      .commentId(commentId)
                                      .userId(commentUpdateRequest.getUserId())
                                      .content(commentUpdateRequest.getContent())
                                      .build();
    }
}
