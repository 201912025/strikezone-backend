package com.strikezone.strikezone_backend.domain.post.dto.response;

import com.strikezone.strikezone_backend.domain.post.entity.Post;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponseDTO {

    private Long postId;
    private String title;
    private String content;
    private String teamName;
    private String username;
    private Integer views;
    private Integer likes;

    public static PostResponseDTO fromEntity(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.postId = post.getPostId();
        dto.title = post.getTitle();
        dto.content = post.getContent();
        dto.teamName = post.getTeam() != null ? post.getTeam().toString() : null;
        dto.username = post.getUser().getUsername();
        dto.views = post.getViews();
        dto.likes = post.getLikes();
        return dto;
    }

    public static List<PostResponseDTO> fromEntities(List<Post> posts) {
        return posts.stream()
                    .map(PostResponseDTO::fromEntity)
                    .collect(Collectors.toList());
    }
}
