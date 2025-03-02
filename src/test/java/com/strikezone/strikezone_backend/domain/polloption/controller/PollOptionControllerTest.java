package com.strikezone.strikezone_backend.domain.polloption.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strikezone.strikezone_backend.domain.polloption.dto.request.controller.PollOptionCreateControllerRequestDto;
import com.strikezone.strikezone_backend.domain.polloption.dto.request.service.PollOptionCreateServiceRequestDto;
import com.strikezone.strikezone_backend.domain.polloption.dto.response.PollOptionResponseDto;
import com.strikezone.strikezone_backend.domain.polloption.service.PollOptionService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PollOptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PollOptionService pollOptionService;

    @Test
    @WithMockUser(value = "testUser", roles = "USER")
    @DisplayName("POST /api/polloptions 요청시 투표 옵션을 생성하면 생성된 옵션 정보를 반환한다")
    void createPollOptionTest() throws Exception {
        // given
        Long pollId = 1L;
        String optionText = "옵션 테스트";
        PollOptionCreateControllerRequestDto requestDto = PollOptionCreateControllerRequestDto.builder()
                                                                                              .pollId(pollId)
                                                                                              .optionText(optionText)
                                                                                              .build();

        PollOptionResponseDto responseDto = PollOptionResponseDto.builder()
                                                                 .optionId(10L)
                                                                 .pollId(pollId)
                                                                 .optionText(optionText)
                                                                 .votesCount(0)
                                                                 .build();

        when(pollOptionService.createPollOption(any(PollOptionCreateServiceRequestDto.class)))
                .thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/polloptions")
                       .contentType("application/json")
                       .content(objectMapper.writeValueAsString(requestDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.optionId").value(10L))
               .andExpect(jsonPath("$.pollId").value(pollId))
               .andExpect(jsonPath("$.optionText").value(optionText))
               .andExpect(jsonPath("$.votesCount").value(0));
    }

    @Test
    @WithMockUser(value = "testUser", roles = "USER")
    @DisplayName("DELETE /api/polloptions/{optionId} 요청시 투표 옵션을 삭제하면 No Content 상태를 반환한다")
    void deletePollOptionTest() throws Exception {
        // given
        Long optionId = 20L;

        // when & then
        mockMvc.perform(delete("/api/polloptions/{optionId}", optionId))
               .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(value = "testUser", roles = "USER")
    @DisplayName("GET /api/polloptions 요청시 투표 옵션 목록을 조회하면 해당 투표의 옵션 목록 정보를 반환한다")
    void getPollOptionsTest() throws Exception {
        // given
        Long pollId = 1L;
        PollOptionResponseDto option1 = PollOptionResponseDto.builder()
                                                             .optionId(101L)
                                                             .pollId(pollId)
                                                             .optionText("옵션 1")
                                                             .votesCount(5)
                                                             .build();

        PollOptionResponseDto option2 = PollOptionResponseDto.builder()
                                                             .optionId(102L)
                                                             .pollId(pollId)
                                                             .optionText("옵션 2")
                                                             .votesCount(3)
                                                             .build();

        List<PollOptionResponseDto> responseList = Arrays.asList(option1, option2);

        when(pollOptionService.getPollOptions(pollId)).thenReturn(responseList);

        // when & then
        mockMvc.perform(get("/api/polloptions")
                       .param("pollId", pollId.toString()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(responseList.size()))
               .andExpect(jsonPath("$[0].optionId").value(101L))
               .andExpect(jsonPath("$[0].optionText").value("옵션 1"))
               .andExpect(jsonPath("$[1].optionId").value(102L))
               .andExpect(jsonPath("$[1].optionText").value("옵션 2"));
    }
}
