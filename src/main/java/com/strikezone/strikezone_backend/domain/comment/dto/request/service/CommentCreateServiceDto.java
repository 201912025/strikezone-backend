package com.strikezone.strikezone_backend.domain.comment.dto.request.service;

import com.strikezone.strikezone_backend.domain.comment.dto.request.controller.CommentCreateRequest;
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

    public static CommentCreateServiceDto from(CommentCreateRequest commentCreateRequest) {
        return CommentCreateServiceDto.builder()
                                      .postId(commentCreateRequest.getPostId())
                                      .userId(commentCreateRequest.getUserId())
                                      .content(commentCreateRequest.getContent())
                                      .build();
    }
}
