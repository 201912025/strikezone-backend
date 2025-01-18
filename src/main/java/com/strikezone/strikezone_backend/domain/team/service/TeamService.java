package com.strikezone.strikezone_backend.domain.team.service;

import com.strikezone.strikezone_backend.domain.team.dto.service.CreateTeamServiceDTO;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import com.strikezone.strikezone_backend.domain.team.exception.TeamExceptionType;
import com.strikezone.strikezone_backend.domain.team.repository.TeamRepository;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.service.UserService;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Transactional
    public Team createTeam(CreateTeamServiceDTO teamServiceDTO) {
        TeamName teamName = TeamName.valueOf(teamServiceDTO.getTeamName().toUpperCase());

        if (teamRepository.existsByName(teamName)) {
            throw new BadRequestException(TeamExceptionType.DUPLICATED_TEAM_NAME);
        }

        Team team = Team.builder()
                .name(teamName)
                .build();
        return teamRepository.save(team);
    }

    @Transactional
    public void deleteTeam(Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new NotFoundException(TeamExceptionType.NOT_FOUND_TEAM);
        }
        teamRepository.deleteById(teamId);
    }

    @Transactional
    public void addUserToTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException(TeamExceptionType.NOT_FOUND_TEAM));

        User user = userService.getUserBySecurity();

        team.addUser(user);
        teamRepository.save(team);
    }

}

/*
    @Transactional
    public void addPlayerToTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException(TeamExceptionType.NOT_FOUND_TEAM));

        team.addPlayer(user);
    }
    */
