package com.strikezone.strikezone_backend.domain.rank.entity;

import com.strikezone.strikezone_backend.domain.player.entity.Player;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rankings")
public class Ranking {

    public Ranking(User user, Team team, Player player, String period) {
        this.user = user;
        this.team = team;
        this.player = player;
        this.period = period;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankingId;

    // 어떤 사용자에 대한 랭킹인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 팀 랭킹(선택)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // 선수 랭킹(선택)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(nullable = false)
    private Integer score = 0;

    @Column(nullable = false, length = 20) // 예: "daily", "weekly", "monthly"
    private String period;

    private LocalDateTime rankedAt = LocalDateTime.now();

}
