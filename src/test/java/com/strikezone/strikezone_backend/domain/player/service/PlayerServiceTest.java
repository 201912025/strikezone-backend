package com.strikezone.strikezone_backend.domain.player.service;

import com.strikezone.strikezone_backend.domain.player.dto.response.CreatePlayerResponseDTO;
import com.strikezone.strikezone_backend.domain.player.dto.service.CreatePlayerRequestServiceDTO;
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
}
