package com.strikezone.strikezone_backend.domain.follow.entity;

import com.strikezone.strikezone_backend.domain.player.entity.Player;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "follows",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "team_id"})})
public class Follow {

    @Builder
    public Follow(User user, Team team, Player player) {
        this.user = user;
        this.team = team;
        this.player = player;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(nullable = false, updatable = false)
    private LocalDateTime followedAt = LocalDateTime.now();

}