package com.strikezone.strikezone_backend.domain.user.entity;

import com.strikezone.strikezone_backend.domain.post.entity.Post;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    private String email;

    private String password;

    @Column(nullable = false)
    private String role;

    private String gender;

    private String birthDay;

    private String bio;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public User(String username, String email, String password, String role, String gender, String birthDay, String bio, Team team) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.birthDay = birthDay;
        this.bio = bio;
        this.team = team;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
