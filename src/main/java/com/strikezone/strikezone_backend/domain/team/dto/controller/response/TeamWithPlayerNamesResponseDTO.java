package com.strikezone.strikezone_backend.domain.team.dto.controller.response;

import com.strikezone.strikezone_backend.domain.player.dto.response.PlayerNameResponseDTO;
import com.strikezone.strikezone_backend.domain.player.entity.Player;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TeamWithPlayerNamesResponseDTO {

    private Long teamId;

    private String teamName;

    private List<PlayerNameResponseDTO> playersNames;

    @Builder
    public TeamWithPlayerNamesResponseDTO(Long teamId, String teamName, List<PlayerNameResponseDTO> playersNames) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.playersNames = playersNames;
    }

    public static TeamWithPlayerNamesResponseDTO from(Team team) {
        List<PlayerNameResponseDTO> playersNames = new ArrayList<>();

        for (Player player : team.getPlayers()) {
            playersNames.add(PlayerNameResponseDTO.from(player));
        }

        return TeamWithPlayerNamesResponseDTO.builder()
                .teamId(team.getTeamId())
                .teamName(team.getName().toString())
                .playersNames(playersNames)
                .build();
    }

}
