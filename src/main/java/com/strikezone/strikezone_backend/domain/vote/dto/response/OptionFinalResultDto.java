package com.strikezone.strikezone_backend.domain.vote.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OptionFinalResultDto {
    private Long optionId;
    private String optionText;
    private Long voteCount;
    private int rank;
}
