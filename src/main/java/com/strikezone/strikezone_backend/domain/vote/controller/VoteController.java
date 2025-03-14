package com.strikezone.strikezone_backend.domain.vote.controller;

import com.strikezone.strikezone_backend.domain.vote.dto.request.controller.VoteCastControllerRequestDto;
import com.strikezone.strikezone_backend.domain.vote.dto.request.service.VoteCastServiceRequestDto;
import com.strikezone.strikezone_backend.domain.vote.dto.response.VoteFinalResultResponseDto;
import com.strikezone.strikezone_backend.domain.vote.dto.response.VoteResponseDto;
import com.strikezone.strikezone_backend.domain.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteResponseDto> castVote(@RequestBody VoteCastControllerRequestDto requestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        VoteCastServiceRequestDto serviceDto = VoteCastServiceRequestDto.from(requestDto, username);
        VoteResponseDto responseDto = voteService.castVote(serviceDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<VoteResponseDto>> getVotesByPoll(@RequestParam Long pollId) {
        List<VoteResponseDto> responseList = voteService.getVotesByPoll(pollId);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/final")
    public ResponseEntity<VoteFinalResultResponseDto> getFinalVoteResults(@RequestParam Long pollId) {
        VoteFinalResultResponseDto finalResults = voteService.getFinalVoteResults(pollId);
        return ResponseEntity.ok(finalResults);
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<Void> cancelVote(@RequestParam Long pollId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        voteService.cancelVote(pollId, userName);
        return ResponseEntity.noContent().build();
    }

}
