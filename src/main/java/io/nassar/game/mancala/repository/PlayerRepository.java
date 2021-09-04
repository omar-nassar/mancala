package io.nassar.game.mancala.repository;

import io.nassar.game.mancala.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
