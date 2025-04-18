package com.strikezone.strikezone_backend.domain.poll.dto.request.service;

import com.strikezone.strikezone_backend.domain.poll.dto.request.controller.PollUpdateControllerRequestDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PollUpdateServiceRequestDto {
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public PollUpdateServiceRequestDto(String title, String description, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static PollUpdateServiceRequestDto from(PollUpdateControllerRequestDto requestDto) {
        return PollUpdateServiceRequestDto.builder()
                                          .title(requestDto.getTitle())
                                          .description(requestDto.getDescription())
                                          .startDate(requestDto.getStartDate())
                                          .endDate(requestDto.getEndDate())
                                          .build();
    }
}
