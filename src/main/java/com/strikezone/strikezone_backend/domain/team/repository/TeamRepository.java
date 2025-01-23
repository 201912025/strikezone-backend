package com.strikezone.strikezone_backend.domain.team.repository;

import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t FROM Team t JOIN FETCH t.players WHERE t.id = :teamId")
    Optional<Team> findByIdWithPlayers(@Param("teamId") Long teamId);

    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.players")
    List<Team> findAllTeamsWithPlayers();

    Optional<Team> findByName(TeamName name);

    boolean existsByName(TeamName name);

}
