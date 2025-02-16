package com.strikezone.strikezone_backend.domain.post.dto.service;

import com.strikezone.strikezone_backend.domain.post.dto.controller.PostRequestDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostRequestServiceDTO {

    private String username;

    private String teamName;

    private String title;

    private String content;

    @Builder
    public PostRequestServiceDTO(String username, String teamName, String title, String content) {
        this.username = username;
        this.teamName = teamName;
        this.title = title;
        this.content = content;
    }

    public static PostRequestServiceDTO from(PostRequestDTO postRequestDTO, String username) {

        return PostRequestServiceDTO.builder()
                                    .username(username)
                                    .teamName(postRequestDTO.getTeamName())
                                    .title(postRequestDTO.getTitle())
                                    .content(postRequestDTO.getContent())
                                    .build();
    }
}
