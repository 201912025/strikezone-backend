package com.strikezone.strikezone_backend.domain.poll.service;

import com.strikezone.strikezone_backend.domain.poll.dto.request.service.PollCreateServiceRequestDto;
import com.strikezone.strikezone_backend.domain.poll.dto.request.service.PollUpdateServiceRequestDto;
import com.strikezone.strikezone_backend.domain.poll.dto.response.PollResponseDto;
import com.strikezone.strikezone_backend.domain.poll.entity.Poll;
import com.strikezone.strikezone_backend.domain.poll.exception.PollExceptionType;
import com.strikezone.strikezone_backend.domain.poll.repository.PollRepository;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PollServiceTest {

    @Mock
    private PollRepository pollRepository;

    @InjectMocks
    private PollService pollService;

    @Test
    @DisplayName("투표 주제를 생성한다")
    void createPollTest() {
        // given
        LocalDateTime now = LocalDateTime.now();
        PollCreateServiceRequestDto createDto = PollCreateServiceRequestDto.builder()
                                                                           .title("테스트 제목")
                                                                           .description("테스트 설명")
                                                                           .startDate(now)
                                                                           .endDate(now.plusDays(1))
                                                                           .build();

        // 엔티티 저장 시 자동생성된 id를 설정하기 위해 ReflectionTestUtils 사용
        Poll poll = Poll.builder()
                        .title(createDto.getTitle())
                        .description(createDto.getDescription())
                        .startDate(createDto.getStartDate())
                        .endDate(createDto.getEndDate())
                        .build();
        ReflectionTestUtils.setField(poll, "pollId", 1L);

        when(pollRepository.save(any(Poll.class))).thenReturn(poll);

        // when
        PollResponseDto response = pollService.createPoll(createDto);

        // then
        assertNotNull(response, "생성된 투표 응답은 null이 아니어야 한다.");
        assertEquals(1L, response.getPollId(), "자동 생성된 id가 1이어야 한다.");
        assertEquals("테스트 제목", response.getTitle(), "제목이 올바르게 설정되어야 한다.");
        assertEquals("테스트 설명", response.getDescription(), "설명이 올바르게 설정되어야 한다.");
        verify(pollRepository, times(1)).save(any(Poll.class));
    }

    @Test
    @DisplayName("투표 주제를 수정한다")
    void updatePollTest() {
        // given 기존 투표 엔티티
        LocalDateTime now = LocalDateTime.now();
        Poll poll = Poll.builder()
                        .title("이전 제목")
                        .description("이전 설명")
                        .startDate(now)
                        .endDate(now.plusDays(1))
                        .build();
        ReflectionTestUtils.setField(poll, "pollId", 2L);

        when(pollRepository.findById(2L)).thenReturn(Optional.of(poll));

        PollUpdateServiceRequestDto updateDto = PollUpdateServiceRequestDto.builder()
                                                                           .title("새 제목")
                                                                           .description("새 설명")
                                                                           .startDate(now.plusDays(1))
                                                                           .endDate(now.plusDays(2))
                                                                           .build();

        // when
        PollResponseDto response = pollService.updatePoll(2L, updateDto);

        // then
        assertNotNull(response, "수정된 투표 응답은 null이 아니어야 한다.");
        assertEquals(2L, response.getPollId(), "투표 id는 2여야 한다.");
        assertEquals("새 제목", response.getTitle(), "수정된 제목이 반영되어야 한다.");
        assertEquals("새 설명", response.getDescription(), "수정된 설명이 반영되어야 한다.");
        verify(pollRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("투표 주제를 삭제한다")
    void deletePollTest() {
        // given 기존 투표 엔티티
        LocalDateTime now = LocalDateTime.now();
        Poll poll = Poll.builder()
                        .title("삭제할 제목")
                        .description("삭제할 설명")
                        .startDate(now)
                        .endDate(now.plusDays(1))
                        .build();
        ReflectionTestUtils.setField(poll, "pollId", 3L);

        when(pollRepository.findById(3L)).thenReturn(Optional.of(poll));

        // when
        pollService.deletePoll(3L);

        // then
        verify(pollRepository, times(1)).delete(poll);
    }

    @Test
    @DisplayName("투표 주제 목록을 조회한다")
    void getPollsTest() {
        // given 여러 개의 투표 엔티티
        LocalDateTime now = LocalDateTime.now();
        Poll poll1 = Poll.builder()
                         .title("제목1")
                         .description("설명1")
                         .startDate(now)
                         .endDate(now.plusDays(1))
                         .build();
        ReflectionTestUtils.setField(poll1, "pollId", 4L);

        Poll poll2 = Poll.builder()
                         .title("제목2")
                         .description("설명2")
                         .startDate(now.plusDays(1))
                         .endDate(now.plusDays(2))
                         .build();
        ReflectionTestUtils.setField(poll2, "pollId", 5L);

        List<Poll> pollList = new ArrayList<>();
        pollList.add(poll1);
        pollList.add(poll2);

        when(pollRepository.findAll()).thenReturn(pollList);

        // when
        List<PollResponseDto> responseList = pollService.getPolls();

        // then
        assertNotNull(responseList, "조회된 투표 목록은 null이 아니어야 한다.");
        assertEquals(2, responseList.size(), "투표 목록 크기는 2여야 한다.");
        assertEquals(4L, responseList.get(0).getPollId(), "첫 번째 투표의 id는 4여야 한다.");
        assertEquals(5L, responseList.get(1).getPollId(), "두 번째 투표의 id는 5여야 한다.");
        verify(pollRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("투표 주제 수정 시 존재하지 않는 경우 예외를 발생시킨다")
    void updatePollNotFoundTest() {
        // given
        when(pollRepository.findById(100L)).thenReturn(Optional.empty());

        PollUpdateServiceRequestDto updateDto = PollUpdateServiceRequestDto.builder()
                                                                           .title("새 제목")
                                                                           .description("새 설명")
                                                                           .startDate(LocalDateTime.now())
                                                                           .endDate(LocalDateTime.now().plusDays(1))
                                                                           .build();

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                pollService.updatePoll(100L, updateDto)
        );
        assertEquals(PollExceptionType.NOT_FOUND_POLL.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("투표 주제 삭제 시 존재하지 않는 경우 예외를 발생시킨다")
    void deletePollNotFoundTest() {
        // given
        when(pollRepository.findById(200L)).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                pollService.deletePoll(200L)
        );
        assertEquals(PollExceptionType.NOT_FOUND_POLL.getMessage(), exception.getMessage());
    }
}
