package com.strikezone.strikezone_backend.domain.vote.service;

import com.strikezone.strikezone_backend.domain.poll.entity.Poll;
import com.strikezone.strikezone_backend.domain.poll.repository.PollRepository;
import com.strikezone.strikezone_backend.domain.polloption.entity.PollOption;
import com.strikezone.strikezone_backend.domain.polloption.repository.PollOptionRepository;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.repository.UserRepository;
import com.strikezone.strikezone_backend.domain.vote.dto.request.service.VoteCastServiceRequestDto;
import com.strikezone.strikezone_backend.domain.vote.dto.response.OptionFinalResultDto;
import com.strikezone.strikezone_backend.domain.vote.dto.response.VoteFinalResultResponseDto;
import com.strikezone.strikezone_backend.domain.vote.dto.response.VoteResponseDto;
import com.strikezone.strikezone_backend.domain.vote.entity.Vote;
import com.strikezone.strikezone_backend.domain.vote.exception.VoteExceptionType;
import com.strikezone.strikezone_backend.domain.vote.repository.VoteRepository;
import com.strikezone.strikezone_backend.global.config.replica.ReadOnlyConnection;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

    private final VoteRepository voteRepository;
    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;
    private final UserRepository userRepository;

    @Transactional
    public VoteResponseDto castVote(VoteCastServiceRequestDto serviceDto) {
        Poll poll = pollRepository.findById(serviceDto.getPollId())
                                  .orElseThrow(() -> new NotFoundException(VoteExceptionType.NOT_FOUND_POLL));

        PollOption option = pollOptionRepository.findById(serviceDto.getOptionId())
                                                .orElseThrow(() -> new NotFoundException(VoteExceptionType.NOT_FOUND_POLLOPTION));

        User user = userRepository.findByUsername(serviceDto.getUserName())
                                  .orElseThrow(() -> new NotFoundException(VoteExceptionType.NOT_FOUND_USER));

        Vote existingVote = voteRepository.findByPollAndUser(poll, user);
        if (existingVote != null) {
            throw new BadRequestException(VoteExceptionType.ALREADY_VOTED);
        }

        option.incrementVotesCount();

        Vote vote = Vote.builder()
                        .user(user)
                        .poll(poll)
                        .option(option)
                        .build();
        Vote savedVote = voteRepository.save(vote);

        return VoteResponseDto.from(savedVote);
    }

    @ReadOnlyConnection
    public List<VoteResponseDto> getVotesByPoll(Long pollId) {
        List<Vote> votes = voteRepository.findByPoll_PollId(pollId);
        return VoteResponseDto.fromEntities(votes);
    }

    @Transactional
    public void cancelVote(Long pollId, String userName) {
        Poll poll = pollRepository.findById(pollId)
                                  .orElseThrow(() -> new NotFoundException(VoteExceptionType.NOT_FOUND_POLL));

        User user = userRepository.findByUsername(userName)
                                  .orElseThrow(() -> new NotFoundException(VoteExceptionType.NOT_FOUND_USER));

        // 사용자가 해당 투표에 참여했는지 확인
        Vote existingVote = voteRepository.findByPollAndUser(poll, user);
        if (existingVote == null) {
            throw new BadRequestException(VoteExceptionType.NOT_VOTED_YET);
        }

        PollOption option = existingVote.getOption();

        voteRepository.delete(existingVote);

        option.decrementVotesCount();
        pollOptionRepository.save(option);
    }

    @ReadOnlyConnection
    public VoteFinalResultResponseDto getFinalVoteResults(Long pollId) {
        List<Vote> votes = voteRepository.findByPoll_PollId(pollId);
        Map<Long, List<Vote>> votesByOption = votes.stream()
                                                   .collect(Collectors.groupingBy(vote -> vote.getOption().getOptionId()));

        List<OptionFinalResultDto> results = votesByOption.entrySet().stream()
                                                          .map(entry -> OptionFinalResultDto.builder()
                                                                                            .optionId(entry.getKey())
                                                                                            .optionText(entry.getValue().get(0).getOption().getOptionText())
                                                                                            .voteCount(entry.getValue().stream().count())
                                                                                            .rank(0) // 초기값; 후에 순위 할당
                                                                                            .build())
                                                          .collect(Collectors.toList());

        // 내림차순 정렬
        results.sort(Comparator.comparingLong(OptionFinalResultDto::getVoteCount).reversed());

        // 순위 할당 (동점 처리)
        int rank = 1;
        for (int i = 0; i < results.size(); i++) {
            if (i > 0 && results.get(i).getVoteCount().equals(results.get(i - 1).getVoteCount())) {
                OptionFinalResultDto previous = results.get(i - 1);
                results.set(i, OptionFinalResultDto.builder()
                                                   .optionId(results.get(i).getOptionId())
                                                   .optionText(results.get(i).getOptionText())
                                                   .voteCount(results.get(i).getVoteCount())
                                                   .rank(previous.getRank())
                                                   .build());
            } else {
                results.set(i, OptionFinalResultDto.builder()
                                                   .optionId(results.get(i).getOptionId())
                                                   .optionText(results.get(i).getOptionText())
                                                   .voteCount(results.get(i).getVoteCount())
                                                   .rank(rank)
                                                   .build());
            }
            rank++;
        }

        return VoteFinalResultResponseDto.builder()
                                         .pollId(pollId)
                                         .finalResults(results)
                                         .build();
    }
}
