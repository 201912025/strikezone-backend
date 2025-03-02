package com.strikezone.strikezone_backend.domain.polloption.repository;

import com.strikezone.strikezone_backend.domain.polloption.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
    List<PollOption> findByPoll_PollId(Long pollId);
}
