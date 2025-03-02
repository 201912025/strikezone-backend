package com.strikezone.strikezone_backend.domain.polloption.service;

import com.strikezone.strikezone_backend.domain.poll.entity.Poll;
import com.strikezone.strikezone_backend.domain.poll.exception.PollExceptionType;
import com.strikezone.strikezone_backend.domain.poll.repository.PollRepository;
import com.strikezone.strikezone_backend.domain.polloption.PollOption;
import com.strikezone.strikezone_backend.domain.polloption.dto.request.service.PollOptionCreateServiceRequestDto;
import com.strikezone.strikezone_backend.domain.polloption.dto.response.PollOptionResponseDto;
import com.strikezone.strikezone_backend.domain.polloption.exception.PollOptionExceptionType;
import com.strikezone.strikezone_backend.domain.polloption.repository.PollOptionRepository;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PollOptionService {

    private final PollOptionRepository pollOptionRepository;
    private final PollRepository pollRepository;

    @Transactional
    public PollOptionResponseDto createPollOption(PollOptionCreateServiceRequestDto serviceDto) {
        // pollId로 Poll 엔티티 조회
        Poll poll = pollRepository.findById(serviceDto.getPollId())
                                  .orElseThrow(() -> new NotFoundException(PollOptionExceptionType.NOT_FOUND_POLL_FOR_OPTION));

        PollOption pollOption = PollOption.builder()
                                          .poll(poll)
                                          .optionText(serviceDto.getOptionText())
                                          .build();

        PollOption savedOption = pollOptionRepository.save(pollOption);
        return PollOptionResponseDto.from(savedOption);
    }

    @Transactional
    public void deletePollOption(Long optionId) {
        PollOption pollOption = pollOptionRepository.findById(optionId)
                                                    .orElseThrow(() -> new NotFoundException(PollOptionExceptionType.NOT_FOUND_POLLOPTION));
        pollOptionRepository.delete(pollOption);
    }

    public List<PollOptionResponseDto> getPollOptions(Long pollId) {
        List<PollOption> options = pollOptionRepository.findByPoll_PollId(pollId);
        return PollOptionResponseDto.fromEntities(options);
    }
}
