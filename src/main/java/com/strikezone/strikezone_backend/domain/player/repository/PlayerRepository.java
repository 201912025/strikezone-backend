package com.strikezone.strikezone_backend.domain.player.repository;

import com.strikezone.strikezone_backend.domain.player.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
