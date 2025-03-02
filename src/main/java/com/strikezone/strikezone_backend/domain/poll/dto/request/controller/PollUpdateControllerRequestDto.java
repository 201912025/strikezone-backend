package com.strikezone.strikezone_backend.domain.poll.dto.request.controller;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PollUpdateControllerRequestDto {
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public PollUpdateControllerRequestDto(String title, String description, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
