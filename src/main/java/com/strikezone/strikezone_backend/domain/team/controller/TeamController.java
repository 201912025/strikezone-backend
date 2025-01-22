package com.strikezone.strikezone_backend.domain.team.controller;

import com.strikezone.strikezone_backend.domain.team.dto.controller.response.TeamWithPlayerNamesResponseDTO;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamWithPlayerNamesResponseDTO> getTeamById(@PathVariable Long teamId) {
        TeamWithPlayerNamesResponseDTO teamDTO = teamService.findTeamByIdAsDTO(teamId);

        return ResponseEntity.ok(teamDTO);
    }

    @GetMapping
    public ResponseEntity<List<TeamWithPlayerNamesResponseDTO>> getAllTeams() {
        List<TeamWithPlayerNamesResponseDTO> teams = teamService.findAllTeamsAsDTO();

        return ResponseEntity.ok(teams);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeamById(@PathVariable Long teamId) {
        teamService.deleteTeamById(teamId);

        return ResponseEntity.noContent().build();
    }
}
