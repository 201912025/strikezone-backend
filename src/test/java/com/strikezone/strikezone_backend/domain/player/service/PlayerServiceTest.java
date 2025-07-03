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
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
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

    @Test @DisplayName("선수 생성 시 저장 후 선수 정보를 반환한다")
    void createPlayer_success() {
        CreatePlayerRequestServiceDTO dto = CreatePlayerRequestServiceDTO.builder()
                                                                         .teamName("KIA").name("김도영").position("3루수").number(5).build();
        Team team = Team.builder().name(TeamName.KIA).build();
        Player saved = Player.builder().name("김도영").team(team).position("3루수").number(5).build();
        ReflectionTestUtils.setField(saved, "playerId", 10L);

        when(teamService.findByTeamName("KIA")).thenReturn(team);
        when(playerRepository.save(any(Player.class)))
                .thenAnswer(inv -> {
                    Player p = inv.getArgument(0);
                    ReflectionTestUtils.setField(p, "playerId", 10L);
                    return p;
                });

        CreatePlayerResponseDTO res = playerService.createPlayer(dto);
        assertEquals(10L, res.getPlayerId());
        assertEquals("KIA", res.getTeamName());
        verify(playerRepository).save(any(Player.class));
    }

    @Test @DisplayName("선수 업데이트 시 변경된 필드만 반영")
    void updatePlayer_success() {
        Long id = 1L;
        Team kia  = Team.builder().name(TeamName.KIA).build();
        Team lg   = Team.builder().name(TeamName.LG).build();
        Player p  = Player.builder().name("김도영").team(kia).position("3루수").number(5).build();
        ReflectionTestUtils.setField(p, "playerId", id);

        UpdatePlayerRequestServiceDTO dto = UpdatePlayerRequestServiceDTO.builder()
                                                                         .playerId(id).name("김도영").teamName("LG").position("2루수").number(7).build();

        when(playerRepository.findById(id)).thenReturn(Optional.of(p));
        when(teamService.findByTeamName("LG")).thenReturn(lg);

        UpdatePlayerResponseDTO res = playerService.updatePlayer(dto);
        assertEquals("LG", res.getTeamName());
        assertEquals("2루수", res.getPosition());
    }

    @Test @DisplayName("updatePlayer: 없는 playerId → NOT_FOUND_PLAYER")
    void updatePlayer_notFound() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());
        UpdatePlayerRequestServiceDTO dto = UpdatePlayerRequestServiceDTO.builder()
                                                                         .playerId(99L).name("x").teamName("KIA").position("P").number(1).build();
        assertThrows(NotFoundException.class, () -> playerService.updatePlayer(dto));
    }

    @Test @DisplayName("playerId로 선수 정보 조회 성공")
    void getPlayer_success() {
        Team team = Team.builder().name(TeamName.KIA).build();
        Player p  = Player.builder().name("김도영").team(team).position("3루수").number(5).build();
        ReflectionTestUtils.setField(p, "playerId", 5L);
        when(playerRepository.findByIdWithTeam(5L)).thenReturn(Optional.of(p));
        PlayerResponseDTO dto = playerService.getPlayer(5L);
        assertEquals("KIA", dto.getTeamName());
        assertEquals("김도영", dto.getName());
    }

    @Test @DisplayName("getPlayer: 없는 playerId 예외")
    void getPlayer_notFound() {
        when(playerRepository.findByIdWithTeam(88L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> playerService.getPlayer(88L));
    }

    @Test @DisplayName("getAllPlayersWithTeam: 전체 목록 DTO 매핑")
    void getAllPlayersWithTeam_success() {
        Team kia = Team.builder().name(TeamName.KIA).build();
        Player p1 = Player.builder().name("A").team(kia).position("C").number(1).build();
        Player p2 = Player.builder().name("B").team(kia).position("P").number(10).build();
        when(playerRepository.findAllWithTeam()).thenReturn(List.of(p1, p2));
        List<PlayerResponseDTO> list = playerService.getAllPlayersWithTeam();
        assertEquals(2, list.size());
        assertEquals("A", list.get(0).getName());
    }
}
