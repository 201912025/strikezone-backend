package com.strikezone.strikezone_backend.domain.poll.service;

import com.strikezone.strikezone_backend.domain.poll.dto.request.service.PollCreateServiceRequestDto;
import com.strikezone.strikezone_backend.domain.poll.dto.request.service.PollUpdateServiceRequestDto;
import com.strikezone.strikezone_backend.domain.poll.dto.response.PollResponseDto;
import com.strikezone.strikezone_backend.domain.poll.entity.Poll;
import com.strikezone.strikezone_backend.domain.poll.exception.PollExceptionType;
import com.strikezone.strikezone_backend.domain.poll.repository.PollRepository;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PollService {

    private final PollRepository pollRepository;

    @Transactional
    public PollResponseDto createPoll(PollCreateServiceRequestDto serviceDto) {
        Poll poll = Poll.builder()
                        .title(serviceDto.getTitle())
                        .description(serviceDto.getDescription())
                        .startDate(serviceDto.getStartDate())
                        .endDate(serviceDto.getEndDate())
                        .build();

        Poll savedPoll = pollRepository.save(poll);
        return PollResponseDto.from(savedPoll);
    }

    @Transactional
    public PollResponseDto updatePoll(Long pollId, PollUpdateServiceRequestDto serviceDto) {
        Poll poll = pollRepository.findById(pollId)
                                  .orElseThrow(() -> new NotFoundException(PollExceptionType.NOT_FOUND_POLL));

        poll.update(
                serviceDto.getTitle(),
                serviceDto.getDescription(),
                serviceDto.getStartDate(),
                serviceDto.getEndDate()
        );
        return PollResponseDto.from(poll);
    }

    @Transactional
    public void deletePoll(Long pollId) {
        Poll poll = pollRepository.findById(pollId)
                                  .orElseThrow(() -> new NotFoundException(PollExceptionType.NOT_FOUND_POLL));
        pollRepository.delete(poll);
    }

    public List<PollResponseDto> getPolls() {
        List<Poll> polls = pollRepository.findAll();
        return PollResponseDto.fromEntities(polls);
    }
}
