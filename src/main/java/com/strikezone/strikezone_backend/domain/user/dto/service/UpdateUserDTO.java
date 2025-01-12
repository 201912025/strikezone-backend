package com.strikezone.strikezone_backend.domain.user.dto.service;

import com.strikezone.strikezone_backend.domain.team.entity.Team;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateUserDTO {

    private String email;
    private String bio;
    private Team team;

    @Builder
    public UpdateUserDTO(String email, String bio, Team team) {
        this.email = email;
        this.bio = bio;
        this.team = team;
    }
}
