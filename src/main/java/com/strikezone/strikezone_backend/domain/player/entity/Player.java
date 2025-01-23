package com.strikezone.strikezone_backend.domain.player.entity;

import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player extends BaseEntity{

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

    @Builder
    public Player(String name, Team team, String position, Integer number, String photoUrl) {
        this.name = name;
        this.team = team;
        this.position = position;
        this.number = number;
    }

    public void changeTeam(Team newTeam) {
        if (this.team != newTeam) {
            this.team = newTeam;
        }
    }

    public void changePosition(String newPosition) {
        if (this.position != newPosition) {
            this.position = newPosition;
        }
    }

    public void changeNumber(Integer newNumber) {
        if (this.number != newNumber) {
            this.number = newNumber;
        }
    }

}
