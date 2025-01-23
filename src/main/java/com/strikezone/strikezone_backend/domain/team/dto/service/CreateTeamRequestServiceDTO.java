package com.strikezone.strikezone_backend.domain.team.dto.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateTeamRequestServiceDTO {

    private Long teamId;

    private String teamName;

    @Builder
    public CreateTeamRequestServiceDTO(Long teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

}
