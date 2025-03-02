package com.strikezone.strikezone_backend.domain.vote.dto.request.service;

import com.strikezone.strikezone_backend.domain.vote.dto.request.controller.VoteCastControllerRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteCastServiceRequestDto {
    private Long pollId;
    private Long optionId;
    private Long userId;

    @Builder
    public VoteCastServiceRequestDto(Long pollId, Long optionId, Long userId) {
        this.pollId = pollId;
        this.optionId = optionId;
        this.userId = userId;
    }

    public static VoteCastServiceRequestDto from(VoteCastControllerRequestDto controllerDto, Long userId) {
        return VoteCastServiceRequestDto.builder()
                                        .pollId(controllerDto.getPollId())
                                        .optionId(controllerDto.getOptionId())
                                        .userId(userId)
                                        .build();
    }
}
