package com.strikezone.strikezone_backend.domain.poll.service;

import com.strikezone.strikezone_backend.domain.poll.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PollService {

    private final PollRepository pollRepository;


}
