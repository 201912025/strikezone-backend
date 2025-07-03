package com.strikezone.strikezone_backend.domain.vote.service;

import com.strikezone.strikezone_backend.domain.poll.entity.Poll;
import com.strikezone.strikezone_backend.domain.poll.repository.PollRepository;
import com.strikezone.strikezone_backend.domain.polloption.entity.PollOption;
import com.strikezone.strikezone_backend.domain.polloption.repository.PollOptionRepository;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.repository.UserRepository;
import com.strikezone.strikezone_backend.domain.vote.dto.request.service.VoteCastServiceRequestDto;
import com.strikezone.strikezone_backend.domain.vote.dto.response.VoteFinalResultResponseDto;
import com.strikezone.strikezone_backend.domain.vote.dto.response.VoteResponseDto;
import com.strikezone.strikezone_backend.domain.vote.entity.Vote;
import com.strikezone.strikezone_backend.domain.vote.repository.VoteRepository;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private PollRepository pollRepository;

    @Mock
    private PollOptionRepository pollOptionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VoteService voteService;

    @DisplayName("사용자가 이미 투표한 경우, 투표를 다시 할 수 없다")
    @Test
    void givenUserHasAlreadyVoted_whenCastVote_thenThrowsBadRequestException() {
        Long pollId = 1L;
        Long optionId = 1L;
        String userName = "testUser";

        Poll poll = Poll.builder()
                        .title("Poll Title")
                        .description("Description")
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .build();

        PollOption option = PollOption.builder()
                                      .poll(poll)
                                      .optionText("Option 1")
                                      .build();

        User user = User.builder()
                        .username(userName)
                        .build();

        Vote existingVote = Vote.builder()
                                .user(user)
                                .poll(poll)
                                .option(option)
                                .build();

        VoteCastServiceRequestDto requestDto = new VoteCastServiceRequestDto(pollId, optionId, userName);

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));
        when(pollOptionRepository.findById(optionId)).thenReturn(Optional.of(option));
        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        when(voteRepository.findByPollAndUser(poll, user)).thenReturn(existingVote); // Already voted

        assertThrows(BadRequestException.class, () -> voteService.castVote(requestDto));
    }

    @DisplayName("사용자가 유효한 투표에 참여하지 않았으면 투표를 취소할 수 없다")
    @Test
    void givenUserHasNotVoted_whenCancelVote_thenThrowsBadRequestException() {
        Long pollId = 1L;
        String userName = "testUser";

        Poll poll = Poll.builder()
                        .title("Poll Title")
                        .description("Description")
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .build();

        User user = User.builder()
                        .username(userName)
                        .build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));
        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        when(voteRepository.findByPollAndUser(poll, user)).thenReturn(null); // No existing vote

        assertThrows(BadRequestException.class, () -> voteService.cancelVote(pollId, userName));
    }

    @DisplayName("사용자가 이미 투표한 경우, 투표를 취소할 수 있다")
    @Test
    void givenUserHasVoted_whenCancelVote_thenVoteIsCancelled() {
        Long pollId = 1L;
        String userName = "testUser";
        Long optionId = 1L;

        Poll poll = Poll.builder()
                        .title("Poll Title")
                        .description("Description")
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .build();

        PollOption option = PollOption.builder()
                                      .poll(poll)
                                      .optionText("Option 1")
                                      .build();

        User user = User.builder()
                        .username(userName)
                        .build();

        Vote existingVote = Vote.builder()
                                .user(user)
                                .poll(poll)
                                .option(option)
                                .build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));
        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        when(voteRepository.findByPollAndUser(poll, user)).thenReturn(existingVote);


        voteService.cancelVote(pollId, userName);

        verify(voteRepository, times(1)).delete(existingVote);
        verify(pollOptionRepository, times(1)).save(option);
    }


    @DisplayName("특정 투표에 대한 모든 투표를 조회할 수 있다")
    @Test
    void givenPoll_whenGetVotesByPoll_thenVotesAreReturned() {
        Long pollId = 1L;

        Poll poll = Poll.builder()
                        .title("Poll Title")
                        .description("Description")
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .build();

        PollOption option = PollOption.builder()
                                      .poll(poll)
                                      .optionText("Option 1")
                                      .build();

        User user = User.builder()
                        .username("user1")
                        .build();

        Vote vote = Vote.builder()
                        .user(user)
                        .poll(poll)
                        .option(option)
                        .build();

        when(voteRepository.findByPoll_PollId(pollId)).thenReturn(List.of(vote));

        List<VoteResponseDto> response = voteService.getVotesByPoll(pollId);

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("castVote: 정상 투표 시 DTO 반환 및 카운트 +1")
    void castVoteSuccess() {
        Long pollId = 1L;
        Long optId  = 10L;
        String userName = "user1";

        Poll poll = Poll.builder()
                        .title("T").description("D")
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .build();

        PollOption option = PollOption.builder()
                                      .poll(poll)
                                      .optionText("A")
                                      .build();
        ReflectionTestUtils.setField(option, "optionId", optId);

        User user = User.builder().username(userName).build();

        Vote saved = Vote.builder()
                         .user(user)
                         .poll(poll)
                         .option(option)
                         .build();

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));
        when(pollOptionRepository.findById(optId)).thenReturn(Optional.of(option));
        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        when(voteRepository.findByPollAndUser(poll, user)).thenReturn(null);
        when(voteRepository.save(any(Vote.class))).thenReturn(saved);

        VoteResponseDto dto = voteService.castVote(
                new VoteCastServiceRequestDto(pollId, optId, userName));

        assertNotNull(dto);
        assertEquals(optId, dto.getOptionId());
        verify(pollOptionRepository, never()).save(option);
        verify(voteRepository).save(any(Vote.class));
        // incrementVotesCount() 호출 여부
        assertEquals(1, option.getVotesCount());
    }

    @Test
    @DisplayName("castVote: Poll ID가 존재하지 않으면 NotFoundException")
    void castVotePollNotFound() {
        when(pollRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> voteService.castVote(new VoteCastServiceRequestDto(1L, 2L, "u")));
    }

    @Test
    @DisplayName("castVote: Option ID가 존재하지 않으면 NotFoundException")
    void castVoteOptionNotFound() {
        Poll poll = Poll.builder().title("T").description("D")
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1)).build();
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(pollOptionRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> voteService.castVote(new VoteCastServiceRequestDto(1L, 2L, "u")));
    }

    @Test
    @DisplayName("castVote: User가 존재하지 않으면 NotFoundException")
    void castVoteUserNotFound() {
        Poll poll   = Poll.builder().title("T").description("D")
                          .startDate(LocalDateTime.now())
                          .endDate(LocalDateTime.now().plusDays(1)).build();
        PollOption opt = PollOption.builder().poll(poll).optionText("A").build();

        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(pollOptionRepository.findById(2L)).thenReturn(Optional.of(opt));
        when(userRepository.findByUsername("nouser")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> voteService.castVote(new VoteCastServiceRequestDto(1L, 2L, "nouser")));
    }

    @Test
    @DisplayName("getFinalVoteResults: 투표 결과 집계 및 순위 부여")
    void getFinalVoteResultsSuccess() {
        Poll poll = Poll.builder()
                        .title("T").description("D")
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(1))
                        .build();

        PollOption optA = PollOption.builder().poll(poll).optionText("A").build();
        PollOption optB = PollOption.builder().poll(poll).optionText("B").build();

        ReflectionTestUtils.setField(optA, "optionId", 1L);
        ReflectionTestUtils.setField(optB, "optionId", 2L);
        User user1 = User.builder().username("u1").build();
        User user2 = User.builder().username("u2").build();

        Vote v1 = Vote.builder().poll(poll).option(optA).user(user1).build();
        Vote v2 = Vote.builder().poll(poll).option(optA).user(user2).build();
        Vote v3 = Vote.builder().poll(poll).option(optB).user(user1).build();

        when(voteRepository.findByPoll_PollId(1L)).thenReturn(List.of(v1, v2, v3));

        VoteFinalResultResponseDto dto = voteService.getFinalVoteResults(1L);

        assertEquals(1L, dto.getPollId());
        assertEquals(2, dto.getFinalResults().size());
        assertEquals("A", dto.getFinalResults().get(0).getOptionText());
        assertEquals(1,  dto.getFinalResults().get(0).getRank());
        assertEquals("B", dto.getFinalResults().get(1).getOptionText());
        assertEquals(2,  dto.getFinalResults().get(1).getRank());
    }
}
