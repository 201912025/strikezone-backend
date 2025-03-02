package com.strikezone.strikezone_backend.domain.polloption.controller;

import com.strikezone.strikezone_backend.domain.polloption.dto.request.controller.PollOptionCreateControllerRequestDto;
import com.strikezone.strikezone_backend.domain.polloption.dto.request.service.PollOptionCreateServiceRequestDto;
import com.strikezone.strikezone_backend.domain.polloption.dto.response.PollOptionResponseDto;
import com.strikezone.strikezone_backend.domain.polloption.service.PollOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polloptions")
@RequiredArgsConstructor
public class PollOptionController {

    private final PollOptionService pollOptionService;

    @PostMapping
    public ResponseEntity<PollOptionResponseDto> createPollOption(@RequestBody PollOptionCreateControllerRequestDto requestDto) {
        PollOptionCreateServiceRequestDto serviceDto = PollOptionCreateServiceRequestDto.from(requestDto);
        PollOptionResponseDto responseDto = pollOptionService.createPollOption(serviceDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deletePollOption(@PathVariable Long optionId) {
        pollOptionService.deletePollOption(optionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PollOptionResponseDto>> getPollOptions(@RequestParam Long pollId) {
        List<PollOptionResponseDto> responseList = pollOptionService.getPollOptions(pollId);
        return ResponseEntity.ok(responseList);
    }
}
