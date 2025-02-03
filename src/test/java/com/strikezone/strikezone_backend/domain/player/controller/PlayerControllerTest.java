package com.strikezone.strikezone_backend.domain.player.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strikezone.strikezone_backend.domain.player.dto.controller.CreatePlayerRequestDTO;
import com.strikezone.strikezone_backend.domain.player.dto.controller.UpdatePlayerRequestDTO;
import com.strikezone.strikezone_backend.domain.player.dto.response.CreatePlayerResponseDTO;
import com.strikezone.strikezone_backend.domain.player.dto.response.PlayerResponseDTO;
import com.strikezone.strikezone_backend.domain.player.dto.response.UpdatePlayerResponseDTO;
import com.strikezone.strikezone_backend.domain.player.service.PlayerService;
import com.strikezone.strikezone_backend.global.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc // MockMvc 자동설정 및 시큐리티 빈 띄움
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("플레이어 생성시 생성된 플레이어 정보를 반환한다.")
    @WithMockUser(value = "kim", roles = "USERS")
    public void testCreatePlayer() throws Exception {
        CreatePlayerRequestDTO requestDTO = CreatePlayerRequestDTO.builder()
                                                                  .name("John Doe")
                                                                  .teamName("TeamA")
                                                                  .position("Pitcher")
                                                                  .number(10)
                                                                  .build();

        CreatePlayerResponseDTO responseDTO = CreatePlayerResponseDTO.builder()
                                                                     .playerId(1L)
                                                                     .name("John Doe")
                                                                     .teamName("TeamA")
                                                                     .position("Pitcher")
                                                                     .number(10)
                                                                     .build();

        when(playerService.createPlayer(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/players")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(requestDTO)))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.playerId").value(1))
               .andExpect(jsonPath("$.name").value("John Doe"))
               .andExpect(jsonPath("$.teamName").value("TeamA"))
               .andExpect(jsonPath("$.position").value("Pitcher"))
               .andExpect(jsonPath("$.number").value(10));
    }

    @Test
    @DisplayName("플레이어 수정시 수정된 플레이어 정보를 반환한다.")
    @WithMockUser(value = "kim", roles = "USERS")
    public void testUpdatePlayer() throws Exception {
        Long playerId = 1L;
        UpdatePlayerRequestDTO requestDTO = UpdatePlayerRequestDTO.builder()
                                                                  .name("John Doe Updated")
                                                                  .teamName("TeamB")
                                                                  .position("Catcher")
                                                                  .number(11)
                                                                  .build();

        UpdatePlayerResponseDTO responseDTO = UpdatePlayerResponseDTO.builder()
                                                                     .playerId(playerId)
                                                                     .name("John Doe Updated")
                                                                     .teamName("TeamB")
                                                                     .position("Catcher")
                                                                     .number(11)
                                                                     .build();

        when(playerService.updatePlayer(any())).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/players/{playerId}", playerId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(requestDTO)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.playerId").value(1))
               .andExpect(jsonPath("$.name").value("John Doe Updated"))
               .andExpect(jsonPath("$.teamName").value("TeamB"))
               .andExpect(jsonPath("$.position").value("Catcher"))
               .andExpect(jsonPath("$.number").value(11));
    }

    @Test
    @DisplayName("플레이어 조회시 해당 플레이어 정보를 반환한다.")
    @WithMockUser(value = "kim", roles = "USERS")
    public void testGetPlayer() throws Exception {

        Long playerId = 1L;
        PlayerResponseDTO responseDTO = PlayerResponseDTO.builder()
                                                         .playerId(playerId)
                                                         .name("John Doe")
                                                         .teamName("TeamA")
                                                         .position("Pitcher")
                                                         .number(10)
                                                         .build();

        when(playerService.getPlayer(playerId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/players/{playerId}", playerId))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.playerId").value(1))
               .andExpect(jsonPath("$.name").value("John Doe"))
               .andExpect(jsonPath("$.teamName").value("TeamA"))
               .andExpect(jsonPath("$.position").value("Pitcher"))
               .andExpect(jsonPath("$.number").value(10));
    }
}
