package com.strikezone.strikezone_backend.domain.vote.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionStatisticsDto {
    private Long optionId;
    private String optionText;
    private Long voteCount;
}
