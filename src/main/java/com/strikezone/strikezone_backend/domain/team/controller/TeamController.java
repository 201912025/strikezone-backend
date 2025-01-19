package com.strikezone.strikezone_backend.domain.team.controller;

import com.strikezone.strikezone_backend.domain.team.dto.controller.response.TeamWithPlayerNamesResponseDTO;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamWithPlayerNamesResponseDTO> getTeamById(@PathVariable Long teamId) {
        Team team = teamService.findTeamById(teamId);

        return ResponseEntity.ok(TeamWithPlayerNamesResponseDTO.from(team));
    }

    @GetMapping
    public ResponseEntity<List<TeamWithPlayerNamesResponseDTO>> getAllTeams() {
        List<Team> teams = teamService.findAllTeams();

        List<TeamWithPlayerNamesResponseDTO> teamWithPlayerNamesResponseDTOS = teams.stream()
                .map(TeamWithPlayerNamesResponseDTO::from).collect(Collectors.toList());

        return ResponseEntity.ok(teamWithPlayerNamesResponseDTOS);
    }

}
