package com.strikezone.strikezone_backend.domain.vote.dto.request.service;

import com.strikezone.strikezone_backend.domain.vote.dto.request.controller.VoteCastControllerRequestDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteCastServiceRequestDto {
    private Long pollId;
    private Long optionId;
    private String userName;

    @Builder
    public VoteCastServiceRequestDto(Long pollId, Long optionId, String userName) {
        this.pollId = pollId;
        this.optionId = optionId;
        this.userName = userName;
    }

    public static VoteCastServiceRequestDto from(VoteCastControllerRequestDto controllerDto, String userName) {
        return VoteCastServiceRequestDto.builder()
                                        .pollId(controllerDto.getPollId())
                                        .optionId(controllerDto.getOptionId())
                                        .userName(userName)
                                        .build();
    }
}
