package com.strikezone.strikezone_backend.domain.polloption.dto.request.service;

import com.strikezone.strikezone_backend.domain.polloption.dto.request.controller.PollOptionCreateControllerRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PollOptionCreateServiceRequestDto {
    private Long pollId;
    private String optionText;

    @Builder
    public PollOptionCreateServiceRequestDto(Long pollId, String optionText) {
        this.pollId = pollId;
        this.optionText = optionText;
    }

    public static PollOptionCreateServiceRequestDto from(PollOptionCreateControllerRequestDto controllerDto) {
        return PollOptionCreateServiceRequestDto.builder()
                                                .pollId(controllerDto.getPollId())
                                                .optionText(controllerDto.getOptionText())
                                                .build();
    }
}
