package com.strikezone.strikezone_backend.domain.post.entity;

import com.strikezone.strikezone_backend.domain.comment.entity.Comment;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false, length = 50)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer views = 0;

    @Column(nullable = false)
    private Integer likes = 0;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(User user, Team team, String title, String content) {
        this.user = user;
        this.team = team;
        this.title = title;
        this.content = content;
    }

    // 연관관계 편의 메서드
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setPost(null);
    }
}
