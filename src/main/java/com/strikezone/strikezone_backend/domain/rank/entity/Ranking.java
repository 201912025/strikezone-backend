package com.strikezone.strikezone_backend.domain.rank.entity;

import com.strikezone.strikezone_backend.domain.player.entity.Player;
import com.strikezone.strikezone_backend.domain.team.Team;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rankings")
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankingId;

    // 어떤 사용자에 대한 랭킹인지
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 팀 랭킹(선택)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "team_id")
    private Team team;

    // 선수 랭킹(선택)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(nullable = false)
    private Integer score = 0;

    @Column(nullable = false, length = 20) // 예: "daily", "weekly", "monthly"
    private String period;

    private LocalDateTime rankedAt = LocalDateTime.now();

}
