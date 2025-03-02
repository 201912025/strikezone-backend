package com.strikezone.strikezone_backend.domain.poll.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strikezone.strikezone_backend.domain.poll.dto.request.controller.PollCreateControllerRequestDto;
import com.strikezone.strikezone_backend.domain.poll.dto.request.controller.PollUpdateControllerRequestDto;
import com.strikezone.strikezone_backend.domain.poll.dto.request.service.PollCreateServiceRequestDto;
import com.strikezone.strikezone_backend.domain.poll.dto.request.service.PollUpdateServiceRequestDto;
import com.strikezone.strikezone_backend.domain.poll.dto.response.PollResponseDto;
import com.strikezone.strikezone_backend.domain.poll.service.PollService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PollControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PollService pollService;

    @Test
    @WithMockUser(value = "testUser", roles = "USER")
    @DisplayName("POST /api/polls 요청시 투표 주제를 생성하면 생성된 투표 정보를 반환한다")
    void createPollTest() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        PollCreateControllerRequestDto requestDto = PollCreateControllerRequestDto.builder()
                                                                                  .title("새로운 투표")
                                                                                  .description("투표 설명")
                                                                                  .startDate(now)
                                                                                  .endDate(now.plusDays(1))
                                                                                  .build();

        PollResponseDto responseDto = PollResponseDto.builder()
                                                     .pollId(1L) // 기본키는 DB에서 자동 생성되었음을 가정
                                                     .title("새로운 투표")
                                                     .description("투표 설명")
                                                     .startDate(now)
                                                     .endDate(now.plusDays(1))
                                                     .build();

        when(pollService.createPoll(any(PollCreateServiceRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/polls")
                       .contentType("application/json")
                       .content(objectMapper.writeValueAsString(requestDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.pollId").value(1L))
               .andExpect(jsonPath("$.title").value("새로운 투표"))
               .andExpect(jsonPath("$.description").value("투표 설명"));
    }

    @Test
    @WithMockUser(value = "testUser", roles = "USER")
    @DisplayName("PUT /api/polls/{pollId} 요청시 투표 주제를 수정하면 수정된 투표 정보를 반환한다")
    void updatePollTest() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        Long pollId = 2L;
        PollUpdateControllerRequestDto requestDto = PollUpdateControllerRequestDto.builder()
                                                                                  .title("수정된 투표")
                                                                                  .description("수정된 설명")
                                                                                  .startDate(now.plusDays(1))
                                                                                  .endDate(now.plusDays(2))
                                                                                  .build();

        PollResponseDto responseDto = PollResponseDto.builder()
                                                     .pollId(pollId)
                                                     .title("수정된 투표")
                                                     .description("수정된 설명")
                                                     .startDate(now.plusDays(1))
                                                     .endDate(now.plusDays(2))
                                                     .build();

        when(pollService.updatePoll(any(Long.class), any(PollUpdateServiceRequestDto.class)))
                .thenReturn(responseDto);

        // when & then
        mockMvc.perform(put("/api/polls/{pollId}", pollId)
                       .contentType("application/json")
                       .content(objectMapper.writeValueAsString(requestDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.pollId").value(pollId))
               .andExpect(jsonPath("$.title").value("수정된 투표"))
               .andExpect(jsonPath("$.description").value("수정된 설명"));
    }

    @Test
    @WithMockUser(value = "testUser", roles = "USER")
    @DisplayName("DELETE /api/polls/{pollId} 요청시 투표 주제를 삭제하면 No Content 상태를 반환한다")
    void deletePollTest() throws Exception {
        // given
        Long pollId = 3L;
        // pollService.deletePoll은 void 반환하므로, 특별한 리턴값 없이 동작함

        // when & then
        mockMvc.perform(delete("/api/polls/{pollId}", pollId))
               .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(value = "testUser", roles = "USER")
    @DisplayName("GET /api/polls 요청시 투표 주제 목록을 조회하면 투표 목록 정보를 반환한다")
    void getPollsTest() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        PollResponseDto responseDto1 = PollResponseDto.builder()
                                                      .pollId(4L)
                                                      .title("투표 제목 1")
                                                      .description("설명 1")
                                                      .startDate(now)
                                                      .endDate(now.plusDays(1))
                                                      .build();

        PollResponseDto responseDto2 = PollResponseDto.builder()
                                                      .pollId(5L)
                                                      .title("투표 제목 2")
                                                      .description("설명 2")
                                                      .startDate(now.plusDays(1))
                                                      .endDate(now.plusDays(2))
                                                      .build();

        List<PollResponseDto> responseList = Arrays.asList(responseDto1, responseDto2);

        when(pollService.getPolls()).thenReturn(responseList);

        // when & then
        mockMvc.perform(get("/api/polls"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(responseList.size()))
               .andExpect(jsonPath("$[0].pollId").value(4L))
               .andExpect(jsonPath("$[0].title").value("투표 제목 1"))
               .andExpect(jsonPath("$[1].pollId").value(5L))
               .andExpect(jsonPath("$[1].title").value("투표 제목 2"));
    }
}
