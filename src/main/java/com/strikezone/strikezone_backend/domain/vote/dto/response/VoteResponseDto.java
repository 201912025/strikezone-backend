package com.strikezone.strikezone_backend.domain.vote.dto.response;

import com.strikezone.strikezone_backend.domain.vote.entity.Vote;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class VoteResponseDto {
    private Long voteId;
    private Long pollId;
    private Long optionId;
    private Long userId;
    private LocalDateTime votedAt;

    public static VoteResponseDto from(Vote vote) {
        return VoteResponseDto.builder()
                              .voteId(vote.getVoteId())
                              .pollId(vote.getPoll().getPollId())
                              .optionId(vote.getOption().getOptionId())
                              .userId(vote.getUser().getUserId())
                              .votedAt(vote.getVotedAt())
                              .build();
    }

    public static List<VoteResponseDto> fromEntities(List<Vote> votes) {
        List<VoteResponseDto> responseList = new ArrayList<>();
        for (Vote vote : votes) {
            responseList.add(from(vote));
        }
        return responseList;
    }
}
