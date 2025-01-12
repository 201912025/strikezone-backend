package com.strikezone.strikezone_backend.domain.user.dto.service;

import com.strikezone.strikezone_backend.domain.team.entity.Team;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinServiceDTO {

    private String username;
    private String password;
    private String email;
    private String role;
    private String gender;
    private String birthDay;
    private String bio;
    private Team team;

    @Builder
    public JoinServiceDTO(String username, String password, String email, String role, String gender, String birthDay, String bio, Team team) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.gender = gender;
        this.birthDay = birthDay;
        this.bio = bio;
        this.team = team;
    }
}
