package com.strikezone.strikezone_backend.domain.player.repository;

import com.strikezone.strikezone_backend.domain.player.entity.Player;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @EntityGraph(attributePaths = "team")
    Optional<Player> findByIdWithTeam(Long playerId);

}
