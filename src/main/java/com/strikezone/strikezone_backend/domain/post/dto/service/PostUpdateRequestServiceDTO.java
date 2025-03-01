package com.strikezone.strikezone_backend.domain.post.dto.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUpdateRequestServiceDTO {

    private Long postId;

    private String username;

    private String title;

    private String content;

    @Builder
    public PostUpdateRequestServiceDTO(Long postId, String username, String title, String content) {
        this.postId = postId;
        this.username = username;
        this.title = title;
        this.content = content;
    }

}
