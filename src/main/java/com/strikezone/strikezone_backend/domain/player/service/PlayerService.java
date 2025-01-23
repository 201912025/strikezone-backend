package com.strikezone.strikezone_backend.domain.player.service;

import com.strikezone.strikezone_backend.domain.player.dto.response.CreatePlayerResponseDTO;
import com.strikezone.strikezone_backend.domain.player.dto.response.UpdatePlayerResponseDTO;
import com.strikezone.strikezone_backend.domain.player.dto.service.CreatePlayerRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.player.dto.service.UpdatePlayerRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.player.entity.Player;
import com.strikezone.strikezone_backend.domain.player.exception.PlayerExceptionType;
import com.strikezone.strikezone_backend.domain.player.repository.PlayerRepository;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.service.TeamService;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final TeamService teamService;

    @Transactional
    public CreatePlayerResponseDTO createPlayer(CreatePlayerRequestServiceDTO serviceDTO) {

        Team team = teamService.findByTeamName(serviceDTO.getTeamName());

        Player player = Player.builder()
                              .name(serviceDTO.getName())
                              .team(team)
                              .position(serviceDTO.getPosition())
                              .number(serviceDTO.getNumber())
                              .build();

        playerRepository.save(player);

        return CreatePlayerResponseDTO.builder()
                .playerId(player.getPlayerId())
                .name(player.getName())
                .teamName(serviceDTO.getTeamName())
                .position(player.getPosition())
                .number(player.getNumber())
                .build();

    }

    @Transactional
    public UpdatePlayerResponseDTO updatePlayer(UpdatePlayerRequestServiceDTO updatePlayerRequestDTO) {
        Player player = playerRepository.findById(updatePlayerRequestDTO.getPlayerId())
                                        .orElseThrow(() -> new NotFoundException(PlayerExceptionType.NOT_FOUND_PLAYER));

        Team team = null;
        if (updatePlayerRequestDTO.getTeamName() != null && !updatePlayerRequestDTO.getTeamName().isEmpty()) {
            team = teamService.findByTeamName(updatePlayerRequestDTO.getTeamName());
        }

        if (updatePlayerRequestDTO.getNumber() != null) {
            player.changeNumber(updatePlayerRequestDTO.getNumber());
        }
        if (updatePlayerRequestDTO.getPosition() != null) {
            player.changePosition(updatePlayerRequestDTO.getPosition());
        }
        if (team != null) {
            player.changeTeam(team);
        }

        playerRepository.save(player);

        return UpdatePlayerResponseDTO.builder()
                                      .playerId(player.getPlayerId())
                                      .name(player.getName())
                                      .teamName(player.getTeam().getName().toString())
                                      .position(player.getPosition())
                                      .number(player.getNumber())
                                      .build();
    }

}
