package com.strikezone.strikezone_backend.domain.team.dto.controller.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateTeamRequestDTO {

    private String teamName;

    @Builder
    public CreateTeamRequestDTO(String teamName) {
        this.teamName = teamName;
    }
}
