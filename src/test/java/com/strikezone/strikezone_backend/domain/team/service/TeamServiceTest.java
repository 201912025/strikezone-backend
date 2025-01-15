package com.strikezone.strikezone_backend.domain.team.service;

import com.strikezone.strikezone_backend.domain.team.dto.service.CreateTeamServiceDTO;
import com.strikezone.strikezone_backend.domain.team.dto.service.UpdateTeamServiceDTO;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import com.strikezone.strikezone_backend.domain.team.repository.TeamRepository;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.service.UserService;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TeamService teamService;

    private CreateTeamServiceDTO createTeamServiceDTO;
    private UpdateTeamServiceDTO updateTeamServiceDTO;
    private Team team;
    private User user;

    @BeforeEach
    void setUp() {
        createTeamServiceDTO = CreateTeamServiceDTO.builder()
                .teamName("두산")
                .build();

        updateTeamServiceDTO = UpdateTeamServiceDTO.builder()
                .teamId(1L)
                .teamName("LG")
                .build();

        team = Team.builder()
                .name(TeamName.두산)
                .build();

        user = User.builder()
                .username("johndoe")
                .password("encodedpassword")
                .email("johndoe@example.com")
                .role("USER")
                .build();
    }

    @Test
    @DisplayName("팀 생성이 성공하는 테스트")
    void createTeamSuccess() {
        // given
        when(teamRepository.existsByName(TeamName.두산)).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // when
        Team createdTeam = teamService.createTeam(createTeamServiceDTO);

        // then
        verify(teamRepository, times(1)).save(any(Team.class));
        assertThat(createdTeam).isNotNull();
        assertThat(createdTeam.getName()).isEqualTo(TeamName.두산);
    }

    @Test
    @DisplayName("이미 존재하는 팀 이름으로 팀 생성 시 예외 발생")
    void createTeamDuplicatedName() {
        // given
        when(teamRepository.existsByName(TeamName.두산)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> teamService.createTeam(createTeamServiceDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("중복된 팀 이름입니다.");
    }

    @Test
    @DisplayName("팀 업데이트가 성공하는 테스트")
    void updateTeamSuccess() {
        // given
        when(teamRepository.findById(updateTeamServiceDTO.getTeamId())).thenReturn(java.util.Optional.of(team));

        // when
        Team updatedTeam = teamService.updateTeam(updateTeamServiceDTO);

        // then
        assertThat(updatedTeam).isNotNull();
        assertThat(updatedTeam.getName()).isEqualTo(TeamName.LG);
    }


    @Test
    @DisplayName("팀 업데이트 시 팀이 존재하지 않으면 예외 발생")
    void updateTeamNotFound() {
        // given
        when(teamRepository.findById(updateTeamServiceDTO.getTeamId())).thenReturn(java.util.Optional.empty());

        // when & then
        assertThatThrownBy(() -> teamService.updateTeam(updateTeamServiceDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("팀을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("팀에 유저 추가 성공")
    void addUserToTeamSuccess() {
        // given
        when(teamRepository.findById(1L)).thenReturn(java.util.Optional.of(team));
        when(userService.getUserBySecurity()).thenReturn(user);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // when
        Team updatedTeam = teamService.addUserToTeam(1L);

        // then
        verify(teamRepository, times(1)).save(any(Team.class));
        assertThat(updatedTeam.getUsers()).contains(user); // assuming there's a method to get users in Team entity
    }

    @Test
    @DisplayName("팀에 유저 추가 시 팀이 존재하지 않으면 예외 발생")
    void addUserToTeamNotFound() {
        // given
        when(teamRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // when & then
        assertThatThrownBy(() -> teamService.addUserToTeam(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("팀을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("팀 삭제 성공")
    void deleteTeamSuccess() {
        // given
        when(teamRepository.existsById(1L)).thenReturn(true);
        doNothing().when(teamRepository).deleteById(1L);

        // when
        teamService.deleteTeam(1L);

        // then
        verify(teamRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("팀 삭제 시 팀이 존재하지 않으면 예외 발생")
    void deleteTeamNotFound() {
        // given
        when(teamRepository.existsById(1L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> teamService.deleteTeam(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("팀을 찾을 수 없습니다.");
    }
}
