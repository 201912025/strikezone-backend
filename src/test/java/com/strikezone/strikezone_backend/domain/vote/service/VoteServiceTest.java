package com.strikezone.strikezone_backend.domain.vote.service;

import com.strikezone.strikezone_backend.domain.poll.entity.Poll;
import com.strikezone.strikezone_backend.domain.poll.repository.PollRepository;
import com.strikezone.strikezone_backend.domain.polloption.entity.PollOption;
import com.strikezone.strikezone_backend.domain.polloption.repository.PollOptionRepository;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.repository.UserRepository;
import com.strikezone.strikezone_backend.domain.vote.dto.request.service.VoteCastServiceRequestDto;
import com.strikezone.strikezone_backend.domain.vote.dto.response.VoteResponseDto;
import com.strikezone.strikezone_backend.domain.vote.entity.Vote;
import com.strikezone.strikezone_backend.domain.vote.repository.VoteRepository;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
