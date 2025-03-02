package com.strikezone.strikezone_backend.domain.poll.controller;

import com.strikezone.strikezone_backend.domain.poll.dto.request.controller.PollCreateControllerRequestDto;
import com.strikezone.strikezone_backend.domain.poll.dto.request.controller.PollUpdateControllerRequestDto;
import com.strikezone.strikezone_backend.domain.poll.dto.request.service.PollCreateServiceRequestDto;
import com.strikezone.strikezone_backend.domain.poll.dto.request.service.PollUpdateServiceRequestDto;
import com.strikezone.strikezone_backend.domain.poll.dto.response.PollResponseDto;
import com.strikezone.strikezone_backend.domain.poll.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polls")
@RequiredArgsConstructor
public class PollController {

    private final PollService pollService;

    @PostMapping
    public ResponseEntity<PollResponseDto> createPoll(@RequestBody PollCreateControllerRequestDto requestDto) {
        PollCreateServiceRequestDto serviceDto = PollCreateServiceRequestDto.from(requestDto);

        PollResponseDto responseDto = pollService.createPoll(serviceDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{pollId}")
    public ResponseEntity<PollResponseDto> updatePoll(@PathVariable Long pollId,
                                                      @RequestBody PollUpdateControllerRequestDto requestDto) {
        PollUpdateServiceRequestDto serviceDto = PollUpdateServiceRequestDto.builder()
                                                                            .title(requestDto.getTitle())
                                                                            .description(requestDto.getDescription())
                                                                            .startDate(requestDto.getStartDate())
                                                                            .endDate(requestDto.getEndDate())
                                                                            .build();

        PollResponseDto responseDto = pollService.updatePoll(pollId, serviceDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{pollId}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long pollId) {
        pollService.deletePoll(pollId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PollResponseDto>> getPolls() {
        List<PollResponseDto> polls =  pollService.getPolls();
        return ResponseEntity.ok(polls);
    }
}
