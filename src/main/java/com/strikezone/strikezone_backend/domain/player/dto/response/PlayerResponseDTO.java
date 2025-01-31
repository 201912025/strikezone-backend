package com.strikezone.strikezone_backend.domain.player.dto.response;

import com.strikezone.strikezone_backend.domain.player.entity.Player;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PlayerResponseDTO {

    private Long playerId;
    private String name;
    private String teamName;
    private String position;
    private Integer number;

    @Builder
    public PlayerResponseDTO(Long playerId, String name, String teamName, String position, Integer number) {
        this.playerId = playerId;
        this.name = name;
        this.teamName = teamName;
        this.position = position;
        this.number = number;
    }

    public static PlayerResponseDTO from(Player player) {
        return PlayerResponseDTO.builder()
                                .playerId(player.getPlayerId())
                                .name(player.getName())
                                .teamName(player.getTeam().toString())
                                .position(player.getPosition())
                                .number(player.getNumber())
                                .build();
    }

}
