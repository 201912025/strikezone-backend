package com.strikezone.strikezone_backend.domain.vote.dto.request.controller;

import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteCastControllerRequestDto {
    private Long pollId;
    private Long optionId;

    @Builder
    public VoteCastControllerRequestDto(Long pollId, Long optionId) {
        this.pollId = pollId;
        this.optionId = optionId;
    }
}
