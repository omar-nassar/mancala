package io.nassar.game.mancala.repository;

import io.nassar.game.mancala.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
