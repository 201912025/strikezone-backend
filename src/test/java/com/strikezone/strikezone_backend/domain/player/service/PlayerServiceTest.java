package com.strikezone.strikezone_backend.domain.player.service;

import com.strikezone.strikezone_backend.domain.player.dto.response.CreatePlayerResponseDTO;
import com.strikezone.strikezone_backend.domain.player.dto.response.PlayerResponseDTO;
import com.strikezone.strikezone_backend.domain.player.dto.response.UpdatePlayerResponseDTO;
import com.strikezone.strikezone_backend.domain.player.dto.service.CreatePlayerRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.player.dto.service.UpdatePlayerRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.player.entity.Player;
import com.strikezone.strikezone_backend.domain.player.repository.PlayerRepository;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import com.strikezone.strikezone_backend.domain.team.service.TeamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private PlayerService playerService;

    @Test
    @DisplayName("선수 생성 시 저장 후 선수 정보를 반환한다.")
    void createPlayer() {
        // given
        CreatePlayerRequestServiceDTO createPlayerRequestServiceDTO = CreatePlayerRequestServiceDTO
                .builder()
                .teamName("KIA")
                .name("김도영")
                .position("3루수")
                .number(5)
                .build();

        Team mockTeam = Team.builder()
                            .name(TeamName.KIA)
                            .build();

        Player mockPlayer = Player.builder()
                                  .name("김도영")
                                  .team(mockTeam)
                                  .position("3루수")
                                  .number(5)
                                  .build();

        CreatePlayerResponseDTO expectedResponse = CreatePlayerResponseDTO
                    .builder()
                    .playerId(mockPlayer.getPlayerId())
                    .teamName(mockPlayer.getTeam().getName().toString())
                    .position(mockPlayer.getPosition())
                    .number(mockPlayer.getNumber())
                    .build();


        when(teamService.findByTeamName("KIA")).thenReturn(mockTeam);

        when(playerRepository.save(any(Player.class))).thenReturn(mockPlayer);

        // when
        CreatePlayerResponseDTO actualResponse = playerService.createPlayer(createPlayerRequestServiceDTO);

        // then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getPlayerId(), actualResponse.getPlayerId());
        assertEquals(expectedResponse.getTeamName(), actualResponse.getTeamName());
        assertEquals(expectedResponse.getPosition(), actualResponse.getPosition());
        assertEquals(expectedResponse.getNumber(), actualResponse.getNumber());

        verify(teamService, times(1)).findByTeamName("KIA");

        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    @DisplayName("선수 업데이트 시 변경된 정보만 반영되고 기존 값은 유지된다.")
    void updatePlayer() {
        // given
        Long playerId = 1L;

        Team existingTeam = Team.builder()
                                .name(TeamName.KIA)
                                .build();

        Player existingPlayer = Player.builder()
                                      .name("김도영")
                                      .team(existingTeam)
                                      .position("3루수")
                                      .number(5)
                                      .build();

        UpdatePlayerRequestServiceDTO updatePlayerRequestServiceDTO = UpdatePlayerRequestServiceDTO.builder()
                                                                                                   .playerId(playerId)
                                                                                                   .name("김도영")
                                                                                                   .teamName("KIA")
                                                                                                   .position("2루수")
                                                                                                   .number(5)
                                                                                                   .build();

        UpdatePlayerResponseDTO expectedResponse = UpdatePlayerResponseDTO.builder()
                                                                          .playerId(playerId)
                                                                          .name("김도영")
                                                                          .teamName(existingPlayer.getTeam().getName().toString())
                                                                          .position("2루수")
                                                                          .number(existingPlayer.getNumber())
                                                                          .build();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(existingPlayer));
        when(teamService.findByTeamName("KIA")).thenReturn(existingTeam);

        // when
        UpdatePlayerResponseDTO actualResponse = playerService.updatePlayer(updatePlayerRequestServiceDTO);

        // then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getPlayerId(), actualResponse.getPlayerId());
        assertEquals(expectedResponse.getName(), actualResponse.getName());
        assertEquals(expectedResponse.getTeamName(), actualResponse.getTeamName());
        assertEquals(expectedResponse.getPosition(), actualResponse.getPosition());
        assertEquals(expectedResponse.getNumber(), actualResponse.getNumber());

        verify(playerRepository, times(1)).findById(playerId);
        verify(teamService, times(1)).findByTeamName("KIA");
    }


    @Test
    @DisplayName("선수 정보를 조회한다.")
    void getPlayer() {
        // given
        Long playerId = 1L;

        Team mockTeam = Team.builder()
                            .name(TeamName.KIA)
                            .build();

        Player mockPlayer = Player.builder()
                                  .name("김도영")
                                  .team(mockTeam)
                                  .position("3루수")
                                  .number(5)
                                  .build();

        PlayerResponseDTO expectedResponse = PlayerResponseDTO.builder()
                                                              .playerId(playerId)
                                                              .teamName(mockPlayer.getTeam().getName().name())
                                                              .name(mockPlayer.getName())
                                                              .position(mockPlayer.getPosition())
                                                              .number(mockPlayer.getNumber())
                                                              .build();

        when(playerRepository.findByIdWithTeam(playerId)).thenReturn(Optional.of(mockPlayer));

        // when
        PlayerResponseDTO actualResponse = playerService.getPlayer(playerId);

        // then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getTeamName(), actualResponse.getTeamName());
        assertEquals(expectedResponse.getPosition(), actualResponse.getPosition());
        assertEquals(expectedResponse.getNumber(), actualResponse.getNumber());

        verify(playerRepository, times(1)).findByIdWithTeam(playerId);
    }

}
