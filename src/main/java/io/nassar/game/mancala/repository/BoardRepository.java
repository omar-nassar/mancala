package io.nassar.game.mancala.repository;

import io.nassar.game.mancala.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}