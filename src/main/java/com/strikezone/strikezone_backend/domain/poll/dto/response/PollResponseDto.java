package com.strikezone.strikezone_backend.domain.poll.dto.response;

import com.strikezone.strikezone_backend.domain.poll.entity.Poll;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PollResponseDto {
    private Long pollId;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public PollResponseDto(Long pollId, String title, String description, LocalDateTime startDate, LocalDateTime endDate) {
        this.pollId = pollId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static PollResponseDto from(Poll poll) {
        return PollResponseDto.builder()
                              .pollId(poll.getPollId())
                              .title(poll.getTitle())
                              .description(poll.getDescription())
                              .startDate(poll.getStartDate())
                              .endDate(poll.getEndDate())
                              .build();
    }

    public static List<PollResponseDto> fromEntities(List<Poll> polls) {
        return polls.stream()
                    .map(PollResponseDto::from)
                    .collect(Collectors.toList());
    }
}
