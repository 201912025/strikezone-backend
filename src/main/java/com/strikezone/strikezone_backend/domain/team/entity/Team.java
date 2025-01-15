package com.strikezone.strikezone_backend.domain.team.entity;

import com.strikezone.strikezone_backend.domain.player.entity.Player;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "teams")
public class Team {

    @Builder
    public Team(TeamName name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Player> players = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamName name;

    public void addUser(User user) {
        if (user != null) {
            this.users.add(user);
            user.setTeam(this);
        }
    }

    public void addPlayer(Player player) {
        if (player != null) {
            players.add(player);
            player.setTeam(this);
        }
    }

    public void changeName(TeamName teamName) {
        this.name = teamName;
    }

}
