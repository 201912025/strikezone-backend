package com.strikezone.strikezone_backend.domain.player.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdatePlayerResponseDTO {

    private Long playerId;

    private String name;

    private String teamName;

    private String position;

    private Integer number;

    @Builder
    public UpdatePlayerResponseDTO(Long playerId, String name, String teamName, String position, Integer number) {
        this.playerId = playerId;
        this.name = name;
        this.teamName = teamName;
        this.position = position;
        this.number = number;
    }

}
