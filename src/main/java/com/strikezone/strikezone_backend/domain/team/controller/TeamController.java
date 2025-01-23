package com.strikezone.strikezone_backend.domain.team.controller;

import com.strikezone.strikezone_backend.domain.team.dto.controller.request.CreateTeamRequestDTO;
import com.strikezone.strikezone_backend.domain.team.dto.response.CreateTeamResponseDTO;
import com.strikezone.strikezone_backend.domain.team.dto.response.TeamWithPlayerNamesResponseDTO;
import com.strikezone.strikezone_backend.domain.team.dto.service.CreateTeamRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @PostMapping("/{teamId}")
    public ResponseEntity<CreateTeamResponseDTO> createTeam(@PathVariable Long teamId, @RequestBody CreateTeamRequestDTO createTeamRequestDTO) {
        CreateTeamRequestServiceDTO serviceDTO = CreateTeamRequestServiceDTO.builder()
                .teamId(teamId)
                .teamName(createTeamRequestDTO.getTeamName())
                .build();

        CreateTeamResponseDTO responseDTO = teamService.createTeam(serviceDTO);

        // 생성된 리소스의 URI (예: /api/teams/{teamId})
        URI location = URI.create("/api/teams/" + responseDTO.getTeamId());

        return ResponseEntity.created(location).body(responseDTO);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeamById(@PathVariable Long teamId) {
        teamService.deleteTeamById(teamId);

        return ResponseEntity.noContent().build();
    }
}
