package com.strikezone.strikezone_backend.domain.vote.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strikezone.strikezone_backend.domain.vote.dto.request.controller.VoteCastControllerRequestDto;
import com.strikezone.strikezone_backend.domain.vote.dto.response.OptionFinalResultDto;
import com.strikezone.strikezone_backend.domain.vote.dto.response.VoteFinalResultResponseDto;
import com.strikezone.strikezone_backend.domain.vote.dto.response.VoteResponseDto;
import com.strikezone.strikezone_backend.domain.vote.service.VoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VoteService voteService;

    @Test
    @WithMockUser(value = "testUser", roles = "USER")
    @DisplayName("투표를 성공적으로 캐스트하면 응답에 VoteResponseDto가 포함된다")
    void castVote_Success_ReturnsVoteResponseDto() throws Exception {
        // Given
        Long pollId = 1L;
        Long optionId = 1L;
        Long userId = 1L;
        String username = "testUser";
        VoteCastControllerRequestDto requestDto = new VoteCastControllerRequestDto(pollId, optionId);
        VoteResponseDto expectedResponseDto = VoteResponseDto.builder()
                                                             .pollId(pollId)
                                                             .optionId(optionId)
                                                             .userId(userId)
                                                             .build();

        when(voteService.castVote(any())).thenReturn(expectedResponseDto);

        // When & Then
        mockMvc.perform(post("/api/votes")
                       .contentType("application/json")
                       .content(objectMapper.writeValueAsString(requestDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.pollId").value(pollId))
               .andExpect(jsonPath("$.optionId").value(optionId))
               .andExpect(jsonPath("$.userId").value(userId));
    }

    @Test
    @WithMockUser(value = "testUser", roles = "USER")
    @DisplayName("설문에 해당하는 모든 투표를 조회하면 VoteResponseDto 리스트를 반환한다")
    void getVotesByPoll_Success_ReturnsVoteResponseDtoList() throws Exception {
        // Given
        Long pollId = 1L;
        List<VoteResponseDto> voteResponseDtoList = Arrays.asList(
                VoteResponseDto.builder().pollId(pollId).optionId(1L).userId(1L).build(),
                VoteResponseDto.builder().pollId(pollId).optionId(2L).userId(2L).build()
        );
        when(voteService.getVotesByPoll(pollId)).thenReturn(voteResponseDtoList);

        // When & Then
        mockMvc.perform(get("/api/votes")
                       .param("pollId", String.valueOf(pollId)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].pollId").value(pollId))
               .andExpect(jsonPath("$[0].optionId").value(1L))
               .andExpect(jsonPath("$[0].userId").value(1L))
               .andExpect(jsonPath("$[1].pollId").value(pollId))
               .andExpect(jsonPath("$[1].optionId").value(2L))
               .andExpect(jsonPath("$[1].userId").value(2L));
    }

    @Test
    @WithMockUser(value = "testUser", roles = "USER")
    @DisplayName("투표 결과를 조회하면 VoteFinalResultResponseDto가 반환된다")
    void getFinalVoteResults_Success_ReturnsVoteFinalResultResponseDto() throws Exception {
        // Given
        Long pollId = 1L;
        List<OptionFinalResultDto> finalResults = Arrays.asList(
                OptionFinalResultDto.builder().optionId(1L).optionText("Option 1").voteCount(10L).rank(1).build(),
                OptionFinalResultDto.builder().optionId(2L).optionText("Option 2").voteCount(5L).rank(2).build()
        );
        VoteFinalResultResponseDto expectedResponseDto = VoteFinalResultResponseDto.builder()
                                                                                   .pollId(pollId)
                                                                                   .finalResults(finalResults)
                                                                                   .build();

        when(voteService.getFinalVoteResults(pollId)).thenReturn(expectedResponseDto);

        // When & Then
        mockMvc.perform(get("/api/votes/final")
                       .param("pollId", String.valueOf(pollId)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.pollId").value(pollId))
               .andExpect(jsonPath("$.finalResults[0].optionId").value(1L))
               .andExpect(jsonPath("$.finalResults[0].optionText").value("Option 1"))
               .andExpect(jsonPath("$.finalResults[0].voteCount").value(10L))
               .andExpect(jsonPath("$.finalResults[0].rank").value(1))
               .andExpect(jsonPath("$.finalResults[1].optionId").value(2L))
               .andExpect(jsonPath("$.finalResults[1].optionText").value("Option 2"))
               .andExpect(jsonPath("$.finalResults[1].voteCount").value(5L))
               .andExpect(jsonPath("$.finalResults[1].rank").value(2));
    }

    @Test
    @WithMockUser(value = "testUser", roles = "USER")
    @DisplayName("투표를 취소하면 204 No Content 응답을 반환한다")
    void cancelVote_Success_ReturnsNoContent() throws Exception {
        // Given
        Long pollId = 1L;
        String username = "testUser";

        // When & Then
        mockMvc.perform(delete("/api/votes/cancel")
                       .param("pollId", String.valueOf(pollId)))
               .andExpect(status().isNoContent());
    }
}
