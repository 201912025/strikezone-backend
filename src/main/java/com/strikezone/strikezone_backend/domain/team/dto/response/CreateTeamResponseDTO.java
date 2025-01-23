package com.strikezone.strikezone_backend.domain.team.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateTeamResponseDTO {

    private Long teamId;

    private String teamName;

    @Builder
    public CreateTeamResponseDTO(Long teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }
}
