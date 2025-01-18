package com.strikezone.strikezone_backend.domain.player.dto.response;

import com.strikezone.strikezone_backend.domain.player.entity.Player;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PlayerNameResponseDTO {

    private String playerName;

    @Builder
    public PlayerNameResponseDTO(String playerName) {
        this.playerName = playerName;
    }

    public static PlayerNameResponseDTO from(Player player) {
        return PlayerNameResponseDTO.builder()
                .playerName(player.getName())
                .build();
    }
}
