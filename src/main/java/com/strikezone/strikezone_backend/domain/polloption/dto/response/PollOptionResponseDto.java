package com.strikezone.strikezone_backend.domain.polloption.dto.response;

import com.strikezone.strikezone_backend.domain.polloption.entity.PollOption;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PollOptionResponseDto {
    private Long optionId;
    private Long pollId;
    private String optionText;
    private Integer votesCount;

    @Builder
    public PollOptionResponseDto(Long optionId, Long pollId, String optionText, Integer votesCount) {
        this.optionId = optionId;
        this.pollId = pollId;
        this.optionText = optionText;
        this.votesCount = votesCount;
    }

    public static PollOptionResponseDto from(PollOption pollOption) {
        return PollOptionResponseDto.builder()
                                    .optionId(pollOption.getOptionId())
                                    .pollId(pollOption.getPoll().getPollId())
                                    .optionText(pollOption.getOptionText())
                                    .votesCount(pollOption.getVotesCount())
                                    .build();
    }

    public static List<PollOptionResponseDto> fromEntities(List<PollOption> pollOptions) {
        List<PollOptionResponseDto> responseDtos = new ArrayList<>();
        for (PollOption option : pollOptions) {
            responseDtos.add(from(option));
        }
        return responseDtos;
    }
}
