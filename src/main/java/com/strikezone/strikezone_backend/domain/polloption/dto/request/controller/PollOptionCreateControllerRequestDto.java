package com.strikezone.strikezone_backend.domain.polloption.dto.request.controller;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PollOptionCreateControllerRequestDto {
    private Long pollId;
    private String optionText;

    @Builder
    public PollOptionCreateControllerRequestDto(Long pollId, String optionText) {
        this.pollId = pollId;
        this.optionText = optionText;
    }
}
