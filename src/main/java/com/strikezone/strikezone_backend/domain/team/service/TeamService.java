package com.strikezone.strikezone_backend.domain.team.service;

import com.strikezone.strikezone_backend.domain.team.dto.response.CreateTeamResponseDTO;
import com.strikezone.strikezone_backend.domain.team.dto.response.TeamWithPlayerNamesResponseDTO;
import com.strikezone.strikezone_backend.domain.team.dto.service.CreateTeamRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import com.strikezone.strikezone_backend.domain.team.exception.TeamExceptionType;
import com.strikezone.strikezone_backend.domain.team.repository.TeamRepository;
import com.strikezone.strikezone_backend.domain.user.service.UserService;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;
    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    public Team findTeamById(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException(TeamExceptionType.NOT_FOUND_TEAM));

        return team;
    }

    public List<TeamWithPlayerNamesResponseDTO> findAllTeamsAsDTO() {
        List<Team> teams = teamRepository.findAllTeamsWithPlayers();

        return teams.stream()
                .map(TeamWithPlayerNamesResponseDTO::from)
                .collect(Collectors.toList());
    }

    public TeamWithPlayerNamesResponseDTO findTeamByIdAsDTO(Long teamId) {
        Team team = teamRepository.findByIdWithPlayers(teamId)
                .orElseThrow(() -> new NotFoundException(TeamExceptionType.NOT_FOUND_TEAM));

        return TeamWithPlayerNamesResponseDTO.from(team);
    }

    @Transactional
    public CreateTeamResponseDTO createTeam(CreateTeamRequestServiceDTO teamServiceDTO) {
        TeamName teamName = TeamName.valueOf(teamServiceDTO.getTeamName().toUpperCase());

        if (teamRepository.existsByName(teamName)) {
            throw new BadRequestException(TeamExceptionType.DUPLICATED_TEAM_NAME);
        }

        Team team = Team.builder()
                .name(teamName)
                .build();

        teamRepository.save(team);

        return CreateTeamResponseDTO.builder()
                .teamId(team.getTeamId())
                .teamName(teamName.toString())
                .build();
    }

    @Transactional
    public void deleteTeamById(Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new NotFoundException(TeamExceptionType.NOT_FOUND_TEAM);
        }

        teamRepository.deleteById(teamId);
    }

}
