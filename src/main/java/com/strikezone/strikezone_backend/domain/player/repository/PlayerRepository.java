package com.strikezone.strikezone_backend.domain.player.repository;

import com.strikezone.strikezone_backend.domain.player.entity.Player;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query("SELECT p FROM Player p JOIN FETCH p.team WHERE p.id = :playerId")
    Optional<Player> findByIdWithTeam(Long playerId);

}
