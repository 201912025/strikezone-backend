package com.strikezone.strikezone_backend.domain.poll.dto.request.service;

import com.strikezone.strikezone_backend.domain.poll.dto.request.controller.PollCreateControllerRequestDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PollCreateServiceRequestDto {
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public PollCreateServiceRequestDto(String title, String description, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static PollCreateServiceRequestDto from(PollCreateControllerRequestDto requestDto) {
        return PollCreateServiceRequestDto.builder()
                                   .title(requestDto.getTitle())
                                   .description(requestDto.getDescription())
                                   .startDate(requestDto.getStartDate())
                                   .endDate(requestDto.getEndDate())
                                   .build();
    }
}
