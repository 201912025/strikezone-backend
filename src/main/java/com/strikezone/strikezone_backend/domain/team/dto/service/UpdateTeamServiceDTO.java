package com.strikezone.strikezone_backend.domain.team.dto.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateTeamServiceDTO {

    private Long teamId;

    private String teamName;

    @Builder
    public UpdateTeamServiceDTO(Long teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

}
