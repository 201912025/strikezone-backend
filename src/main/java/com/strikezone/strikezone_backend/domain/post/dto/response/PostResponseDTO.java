package com.strikezone.strikezone_backend.domain.post.dto.response;

import com.strikezone.strikezone_backend.domain.post.entity.Post;
import lombok.Builder;
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

    @Builder
    public PostResponseDTO(Long postId, String title, String content, String teamName, String username, Integer views, Integer likes) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.teamName = teamName;
        this.username = username;
        this.views = views;
        this.likes = likes;
    }

    public static PostResponseDTO fromEntity(Post post) {
        return PostResponseDTO.builder()
                              .postId(post.getPostId())
                              .title(post.getTitle())
                              .content(post.getContent())
                              .teamName(post.getTeam() != null ? post.getTeam().getName().toString() : null)
                              .username(post.getUser().getUsername())
                              .views(post.getViews())
                              .likes(post.getLikes())
                              .build();
    }


    public static List<PostResponseDTO> fromEntities(List<Post> posts) {
        return posts.stream()
                    .map(PostResponseDTO::fromEntity)
                    .collect(Collectors.toList());
    }
}
