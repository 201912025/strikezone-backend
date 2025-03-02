package com.strikezone.strikezone_backend.domain.vote.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VoteFinalResultResponseDto {
    private Long pollId;
    private List<OptionFinalResultDto> finalResults;
}
