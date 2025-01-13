package com.strikezone.strikezone_backend.domain.player.entity;

import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "players")
public class Player extends BaseEntity{

    @Builder
    public Player(String name, Team team, String position, Integer number, String photoUrl) {
        this.name = name;
        this.team = team;
        this.position = position;
        this.number = number;
        this.photoUrl = photoUrl;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerId;

    @Column(nullable = false, length = 30)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false, length = 10)
    private String position;

    @Column(nullable = false, length = 3)
    private Integer number;

    @Column(length = 255)
    private String photoUrl;

}
