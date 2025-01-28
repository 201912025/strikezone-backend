package com.strikezone.strikezone_backend.domain.player.controller;

import com.strikezone.strikezone_backend.domain.player.dto.controller.CreatePlayerRequestDTO;
import com.strikezone.strikezone_backend.domain.player.dto.controller.UpdatePlayerRequestDTO;
import com.strikezone.strikezone_backend.domain.player.dto.response.CreatePlayerResponseDTO;
import com.strikezone.strikezone_backend.domain.player.dto.response.PlayerResponseDTO;
import com.strikezone.strikezone_backend.domain.player.dto.response.UpdatePlayerResponseDTO;
import com.strikezone.strikezone_backend.domain.player.dto.service.CreatePlayerRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.player.dto.service.UpdatePlayerRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    public ResponseEntity<CreatePlayerResponseDTO> createPlayer(@RequestBody CreatePlayerRequestDTO createPlayerRequestDTO) {
        CreatePlayerRequestServiceDTO serviceDTO = CreatePlayerRequestServiceDTO.builder()
                                                                                .name(createPlayerRequestDTO.getName())
                                                                                .teamName(createPlayerRequestDTO.getTeamName())
                                                                                .position(createPlayerRequestDTO.getPosition())
                                                                                .number(createPlayerRequestDTO.getNumber())
                                                                                .build();

        CreatePlayerResponseDTO response = playerService.createPlayer(serviceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{playerId}")
    public ResponseEntity<UpdatePlayerResponseDTO> updatePlayer(@PathVariable Long playerId,
                                                                @RequestBody UpdatePlayerRequestDTO updatePlayerRequestDTO) {
        UpdatePlayerRequestServiceDTO serviceDTO = UpdatePlayerRequestServiceDTO.builder()
                                                                                .playerId(playerId)
                                                                                .name(updatePlayerRequestDTO.getName())
                                                                                .teamName(updatePlayerRequestDTO.getTeamName())
                                                                                .position(updatePlayerRequestDTO.getPosition())
                                                                                .number(updatePlayerRequestDTO.getNumber())
                                                                                .build();

        UpdatePlayerResponseDTO response = playerService.updatePlayer(serviceDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerResponseDTO> getPlayer(@PathVariable Long playerId) {
        PlayerResponseDTO response = playerService.getPlayer(playerId);
        return ResponseEntity.ok(response);
    }

}
