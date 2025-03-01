package com.strikezone.strikezone_backend.domain.post.dto.controller;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUpdateRequestDTO {

    private String title;

    private String content;

    @Builder
    public PostUpdateRequestDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
