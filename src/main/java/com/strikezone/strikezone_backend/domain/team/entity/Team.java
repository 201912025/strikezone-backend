package com.strikezone.strikezone_backend.domain.team.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "teams")
public class Team {

    public Team(TeamName name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamName name;

}
