package com.strikezone.strikezone_backend.domain.team.dto.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateTeamServiceDTO {

    private String teamName;

    @Builder
    public CreateTeamServiceDTO(String teamName) {
        this.teamName = teamName;
    }
    
}
