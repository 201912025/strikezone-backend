package com.strikezone.strikezone_backend.domain.team.repository;

import com.strikezone.strikezone_backend.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

}
