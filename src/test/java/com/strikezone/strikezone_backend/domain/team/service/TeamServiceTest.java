package com.strikezone.strikezone_backend.domain.team.service;

import com.strikezone.strikezone_backend.domain.team.dto.service.CreateTeamServiceDTO;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import com.strikezone.strikezone_backend.domain.team.repository.TeamRepository;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.service.UserService;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

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

    @Test
    @DisplayName("팀 생성이 성공하는 테스트")
    void createTeamSuccess() {
        // given
        Team team = Team.builder()
                .name(TeamName.두산)
                .build();

        CreateTeamServiceDTO createTeamServiceDTO = CreateTeamServiceDTO.builder()
                .teamName("두산")
                .build();

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
    @DisplayName("특정 팀을 가져올시 팀 반환 성공하는 테스트 ")
    void getTeamSuccess() {
        //given
        Team team = Team.builder()
                .name(TeamName.두산)
                .build();

        //when
        when(teamRepository.findById(1L)).thenReturn(java.util.Optional.of(team));

        Team testTeam = teamService.findTeamById(1L);

        assertThat(testTeam).isNotNull();
        assertThat(testTeam.getName()).isEqualTo(TeamName.두산);
    }

    @Test
    @DisplayName("모든 팀을 가져올시 모든팀 반환 성공하는 테스트")
    void getAllTeamsSuccess() {
        // given
        Team team1 = Team.builder()
                .name(TeamName.두산)
                .build();
        Team team2 = Team.builder()
                .name(TeamName.LG)
                .build();

        when(teamRepository.findAll()).thenReturn(Arrays.asList(team1, team2));

        // when
        List<Team> teams = teamService.findAllTeams();

        // then
        verify(teamRepository, times(1)).findAll();
        assertThat(teams).hasSize(2);
        assertThat(teams).contains(team1, team2);
    }

    @Test
    @DisplayName("이미 존재하는 팀 이름으로 팀 생성 시 예외 발생")
    void createTeamDuplicatedName() {
        // given
        CreateTeamServiceDTO createTeamServiceDTO = CreateTeamServiceDTO.builder()
                .teamName("두산")
                .build();

        when(teamRepository.existsByName(TeamName.두산)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> teamService.createTeam(createTeamServiceDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("중복된 팀 이름입니다.");
    }

    @Test
    @DisplayName("팀에 유저 추가 성공")
    void addUserToTeamSuccess() {
        // given
        Team team = Team.builder()
                .name(TeamName.두산)
                .build();

        User user = User.builder()
                .username("johndoe")
                .password("encodedpassword")
                .email("johndoe@example.com")
                .role("USER")
                .build();

        // SecurityContext에 사용자 설정
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        when(teamRepository.findById(1L)).thenReturn(java.util.Optional.of(team));
        when(userService.getUserBySecurity()).thenReturn(user);

        // when
        teamService.addUserToTeam(1L);

        // then
        verify(teamRepository, times(1)).save(team);
        assertThat(team.getUsers()).contains(user);
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
        teamService.deleteTeamById(1L);

        // then
        verify(teamRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("팀 삭제 시 팀이 존재하지 않으면 예외 발생")
    void deleteTeamNotFound() {
        // given
        when(teamRepository.existsById(1L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> teamService.deleteTeamById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("팀을 찾을 수 없습니다.");
    }
}
