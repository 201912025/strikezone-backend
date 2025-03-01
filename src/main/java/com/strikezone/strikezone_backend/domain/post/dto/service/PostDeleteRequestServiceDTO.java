package com.strikezone.strikezone_backend.domain.post.dto.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDeleteRequestServiceDTO {

    private Long postId;

    private String username;

    @Builder
    public PostDeleteRequestServiceDTO(Long postId, String username) {
        this.postId = postId;
        this.username = username;
    }
}
