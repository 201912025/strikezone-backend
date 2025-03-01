package com.strikezone.strikezone_backend.domain.post.dto.service;

import com.strikezone.strikezone_backend.domain.post.dto.controller.PostUpdateRequestDTO;
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

    public static PostUpdateRequestServiceDTO from(PostUpdateRequestDTO postUpdateRequestDTO, Long postId, String username) {
        return PostUpdateRequestServiceDTO.builder()
                .postId(postId)
                .username(username)
                .title(postUpdateRequestDTO.getTitle())
                .content(postUpdateRequestDTO.getContent())
                .build();
    }

}
