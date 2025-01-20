package com.strikezone.strikezone_backend.domain.team.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        Team team = Team.builder()
                .name(TeamName.두산).build();

        when(teamService.findTeamById(1L)).thenReturn(team);

        mockMvc.perform(get("/api/teams/1"))
                .andExpect(status().isOk())  // 응답 상태 코드 확인
                .andExpect(jsonPath("$.teamName").value("두산"));  // 응답 내용 검증

        verify(teamService, times(1)).findTeamById(1L);
    }

    @Test
    @DisplayName("조회시 모든 팀을 반환한다.")
    @WithMockUser(value = "kim", roles = "USERS")
    void getAllTeams() throws Exception {
        Team team1 = Team.builder()
                .name(TeamName.KIA)
                .build();

        Team team2 = Team.builder()
                .name(TeamName.LG)
                .build();

        when(teamService.findAllTeams()).thenReturn(Arrays.asList(team1, team2));

        mockMvc.perform(get("/api/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teamId").value(team1.getTeamId()))
                .andExpect(jsonPath("$[0].teamName").value(team1.getName().toString()))
                .andExpect(jsonPath("$[1].teamId").value(team2.getTeamId()))
                .andExpect(jsonPath("$[1].teamName").value(team2.getName().toString()))
                .andExpect(jsonPath("$[0].playersNames").isEmpty())
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
