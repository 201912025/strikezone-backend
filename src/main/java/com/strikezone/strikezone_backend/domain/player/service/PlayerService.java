package com.strikezone.strikezone_backend.domain.player.service;

import com.strikezone.strikezone_backend.domain.player.dto.response.CreatePlayerResponseDTO;
import com.strikezone.strikezone_backend.domain.player.dto.service.CreatePlayerRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.player.entity.Player;
import com.strikezone.strikezone_backend.domain.player.repository.PlayerRepository;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayerService {

    private final PlayerRepository playerRepository;

    private final TeamService teamService;

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

}
