package com.strikezone.strikezone_backend.domain.polloption.service;

import com.strikezone.strikezone_backend.domain.poll.entity.Poll;
import com.strikezone.strikezone_backend.domain.polloption.PollOption;
import com.strikezone.strikezone_backend.domain.polloption.dto.request.service.PollOptionCreateServiceRequestDto;
import com.strikezone.strikezone_backend.domain.polloption.dto.response.PollOptionResponseDto;
import com.strikezone.strikezone_backend.domain.polloption.exception.PollOptionExceptionType;
import com.strikezone.strikezone_backend.domain.polloption.repository.PollOptionRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PollOptionServiceTest {

    @Mock
    private PollOptionRepository pollOptionRepository;

    @Mock
    private PollRepository pollRepository;

    @InjectMocks
    private PollOptionService pollOptionService;

    @Test
    @DisplayName("투표 옵션을 생성하면 생성된 옵션 정보를 반환한다")
    void createPollOptionTest() {
        // given
        Long pollId = 1L;
        String optionText = "옵션 A";
        LocalDateTime now = LocalDateTime.now();

        PollOptionCreateServiceRequestDto serviceDto = PollOptionCreateServiceRequestDto.builder()
                                                                                        .pollId(pollId)
                                                                                        .optionText(optionText)
                                                                                        .build();

        // Poll 엔티티 생성 (기본키는 자동 생성되므로 테스트 시 강제 주입)
        Poll poll = Poll.builder()
                        .title("투표 제목")
                        .description("투표 설명")
                        .startDate(now)
                        .endDate(now.plusDays(1))
                        .build();
        ReflectionTestUtils.setField(poll, "pollId", pollId);

        // PollOption 엔티티 생성
        PollOption pollOption = PollOption.builder()
                                          .poll(poll)
                                          .optionText(optionText)
                                          .build();
        ReflectionTestUtils.setField(pollOption, "optionId", 10L);

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));
        when(pollOptionRepository.save(any(PollOption.class))).thenReturn(pollOption);

        // when
        PollOptionResponseDto responseDto = pollOptionService.createPollOption(serviceDto);

        // then
        assertNotNull(responseDto, "생성된 투표 옵션 정보는 null이 아니어야 한다.");
        assertEquals(10L, responseDto.getOptionId(), "자동 생성된 옵션 id는 10이어야 한다.");
        assertEquals(pollId, responseDto.getPollId(), "연결된 투표의 id가 올바르게 반환되어야 한다.");
        assertEquals(optionText, responseDto.getOptionText(), "투표 옵션 텍스트가 올바르게 반환되어야 한다.");
        verify(pollRepository, times(1)).findById(pollId);
        verify(pollOptionRepository, times(1)).save(any(PollOption.class));
    }

    @Test
    @DisplayName("투표 옵션 생성 시 해당 투표가 없으면 예외를 발생시킨다")
    void createPollOptionNotFoundTest() {
        // given
        Long pollId = 100L;
        String optionText = "옵션 B";
        PollOptionCreateServiceRequestDto serviceDto = PollOptionCreateServiceRequestDto.builder()
                                                                                        .pollId(pollId)
                                                                                        .optionText(optionText)
                                                                                        .build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> pollOptionService.createPollOption(serviceDto),
                "해당 투표가 없으면 NotFoundException이 발생해야 한다.");
        assertEquals(PollOptionExceptionType.NOT_FOUND_POLL_FOR_OPTION.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("투표 옵션을 삭제하면 옵션이 삭제된다")
    void deletePollOptionTest() {
        // given
        Long optionId = 20L;
        Poll poll = Poll.builder()
                        .title("투표 제목")
                        .description("투표 설명")
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .build();
        ReflectionTestUtils.setField(poll, "pollId", 1L);

        PollOption pollOption = PollOption.builder()
                                          .poll(poll)
                                          .optionText("옵션 C")
                                          .build();
        ReflectionTestUtils.setField(pollOption, "optionId", optionId);

        when(pollOptionRepository.findById(optionId)).thenReturn(Optional.of(pollOption));

        // when
        pollOptionService.deletePollOption(optionId);

        // then
        verify(pollOptionRepository, times(1)).delete(pollOption);
    }

    @Test
    @DisplayName("투표 옵션 삭제 시 옵션이 없으면 예외를 발생시킨다")
    void deletePollOptionNotFoundTest() {
        // given
        Long optionId = 30L;
        when(pollOptionRepository.findById(optionId)).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> pollOptionService.deletePollOption(optionId),
                "해당 옵션이 없으면 NotFoundException이 발생해야 한다.");
        assertEquals(PollOptionExceptionType.NOT_FOUND_POLLOPTION.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("투표 옵션 목록을 조회하면 해당 투표의 옵션 목록을 반환한다")
    void getPollOptionsTest() {
        // given
        Long pollId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Poll poll = Poll.builder()
                        .title("투표 제목")
                        .description("투표 설명")
                        .startDate(now)
                        .endDate(now.plusDays(1))
                        .build();
        ReflectionTestUtils.setField(poll, "pollId", pollId);

        PollOption option1 = PollOption.builder()
                                       .poll(poll)
                                       .optionText("옵션 1")
                                       .build();
        ReflectionTestUtils.setField(option1, "optionId", 101L);

        PollOption option2 = PollOption.builder()
                                       .poll(poll)
                                       .optionText("옵션 2")
                                       .build();
        ReflectionTestUtils.setField(option2, "optionId", 102L);

        List<PollOption> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        when(pollOptionRepository.findByPoll_PollId(pollId)).thenReturn(options);

        // when
        List<PollOptionResponseDto> responseList = pollOptionService.getPollOptions(pollId);

        // then
        assertNotNull(responseList, "조회된 투표 옵션 목록은 null이 아니어야 한다.");
        assertEquals(2, responseList.size(), "투표 옵션 목록의 크기는 2여야 한다.");
        assertEquals(101L, responseList.get(0).getOptionId(), "첫 번째 옵션 id가 올바르게 반환되어야 한다.");
        assertEquals(102L, responseList.get(1).getOptionId(), "두 번째 옵션 id가 올바르게 반환되어야 한다.");
        verify(pollOptionRepository, times(1)).findByPoll_PollId(pollId);
    }
}
