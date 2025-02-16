package com.strikezone.strikezone_backend.domain.post.dto.controller;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostRequestDTO {

    private String teamName;

    private String title;

    private String content;

    @Builder
    public PostRequestDTO(String teamName, String title, String content) {
        this.teamName = teamName;
        this.title = title;
        this.content = content;
    }

}
