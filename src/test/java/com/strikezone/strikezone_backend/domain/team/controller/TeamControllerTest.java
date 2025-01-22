package com.strikezone.strikezone_backend.domain.team.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strikezone.strikezone_backend.domain.team.dto.controller.response.TeamWithPlayerNamesResponseDTO;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import com.strikezone.strikezone_backend.domain.team.service.TeamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc // MockMvc 자동설정 및 시큐리티 빈 띄움
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TeamService teamService;

    @Test
    @DisplayName("특정 팀 조회시 특정 팀을 반환한다.")
    @WithMockUser(value = "kim", roles = "USERS")
    void getTeam() throws Exception {
        TeamWithPlayerNamesResponseDTO teamDTO = TeamWithPlayerNamesResponseDTO.builder()
                .teamName("두산")
                .build();

        when(teamService.findTeamByIdAsDTO(1L)).thenReturn(teamDTO);

        mockMvc.perform(get("/api/teams/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamName").value("두산"));

        verify(teamService, times(1)).findTeamByIdAsDTO(1L);
    }


    @Test
    @DisplayName("조회시 모든 팀을 반환한다.")
    @WithMockUser(value = "kim", roles = "USERS")
    void getAllTeams() throws Exception {
        TeamWithPlayerNamesResponseDTO team1DTO = TeamWithPlayerNamesResponseDTO.builder()
                .teamId(1L)
                .teamName("KIA")
                .playersNames(Collections.emptyList())
                .build();

        TeamWithPlayerNamesResponseDTO team2DTO = TeamWithPlayerNamesResponseDTO.builder()
                .teamId(2L)
                .teamName("LG")
                .playersNames(Collections.emptyList())
                .build();

        when(teamService.findAllTeamsAsDTO()).thenReturn(Arrays.asList(team1DTO, team2DTO));

        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teamId").value(team1DTO.getTeamId()))
                .andExpect(jsonPath("$[0].teamName").value(team1DTO.getTeamName()))
                .andExpect(jsonPath("$[0].playersNames").isEmpty())
                .andExpect(jsonPath("$[1].teamId").value(team2DTO.getTeamId()))
                .andExpect(jsonPath("$[1].teamName").value(team2DTO.getTeamName()))
                .andExpect(jsonPath("$[1].playersNames").isEmpty());
    }


    @Test
    @DisplayName("특정 팀 삭제시 No Content 상태 코드가 반환된다.")
    @WithMockUser(value = "kim", roles = "USERS")
    void deleteTeam() throws Exception {
        Long teamId = 1L;

        doNothing().when(teamService).deleteTeamById(teamId);

        mockMvc.perform(delete("/api/teams/{teamId}", teamId))
                .andExpect(status().isNoContent());

        verify(teamService, times(1)).deleteTeamById(teamId);
    }


}
