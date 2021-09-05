package io.nassar.game.mancala;

import io.nassar.game.mancala.domain.Game;
import io.nassar.game.mancala.domain.Pit;
import io.nassar.game.mancala.domain.Player;
import io.nassar.game.mancala.exception.BusinessException;
import io.nassar.game.mancala.service.GameService;
import io.nassar.game.mancala.service.PitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MancalaApiApplicationTests {

	@Autowired
	GameService gameService;

	@Test
	void createGame() {
		Game game = gameService.createNewGame("p1", "p2");

		assertNotNull(game.getId());
		assertNotNull(game.getCreatedAt());
		assertNotNull(game.getPlayerTurn());

		assertNotNull(game.getPits());

		assertEquals(14, game.getPits().size());

		assertEquals(2, game.getPits().stream().filter(Pit::getIsBigPit).count());
		assertIterableEquals(Arrays.asList(7,14),
							 game.getPits().stream()
									 		.filter(Pit::getIsBigPit)
									 		.map(Pit::getIndex)
									 		.collect(Collectors.toList()));

		assertEquals(12, game.getPits().stream().filter((p) -> !p.getIsBigPit()).count());
		assertIterableEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13),
							 game.getPits().stream()
									.filter((p) -> !p.getIsBigPit())
									.map(Pit::getIndex)
									.collect(Collectors.toList()));

	}

	@Test
	void validateUserTurnToPlay() {
		Game game = gameService.createNewGame("p1", "p2");

		Player playerTurn = game.getPlayerTurn();
		Pit otherPlayerPit = game.getPits()
									.stream()
									.filter(p -> !p.getPlayer().getId().equals(playerTurn.getId()))
									.findFirst()
									.get();

		assertThrows(BusinessException.class, () -> gameService.move(game.getId(), otherPlayerPit.getIndex()));
	}

	@Test
	void playOnEmptyPit() {
		Game game = gameService.createNewGame("p1", "p2");

		Player playerTurn = game.getPlayerTurn();

		Pit otherPlayerPit = game.getPits()
									.stream()
									.filter(p -> p.getPlayer().getId().equals(playerTurn.getId()))
									.findFirst()
									.get();

		otherPlayerPit.setStoneCount(0);

		assertThrows(BusinessException.class, () -> gameService.move(game.getId(), otherPlayerPit.getIndex()));
	}

	@Test
	void playFromBigPit() {
		Game game = gameService.createNewGame("p1", "p2");

		Player playerTurn = game.getPlayerTurn();

		final Pit firstBigPit = game.getPits().get(PitService.BIG_PIT_1_INDEX);

		Pit myBigPit = firstBigPit.getPlayer().getId().equals(playerTurn.getId()) ? firstBigPit :
																					game.getPits().get(PitService.BIG_PIT_2_INDEX);

		assertThrows(BusinessException.class, () -> gameService.move(game.getId(), myBigPit.getIndex()));
	}

	@Test
	void sowFirstPitThatEndIntoBigPitAndKeepSamePlayerTurn() {
		Game game = gameService.createNewGame("p1", "p2");

		Long currentPlayer = game.getPlayerTurn().getId();

		gameService.sow(game.getPits(), 0);

		assertEquals(0, game.getPits().get(0).getStoneCount());
		assertEquals(7, game.getPits().get(1).getStoneCount());
		assertEquals(7, game.getPits().get(2).getStoneCount());
		assertEquals(7, game.getPits().get(3).getStoneCount());
		assertEquals(7, game.getPits().get(4).getStoneCount());
		assertEquals(7, game.getPits().get(5).getStoneCount());
		assertEquals(1, game.getPits().get(6).getStoneCount());

		assertEquals(currentPlayer, game.getPlayerTurn().getId());
	}

	@Test
	void sowSecondPitThatEndIntoOpponentPitAndSwitchPlayerTurn() {
		Game game = gameService.createNewGame("p1", "p2");

		Long currentPlayer = game.getPlayerTurn().getId();

		gameService.sow(game.getPits(), 0);
		gameService.sow(game.getPits(), 1);

		assertEquals(0, game.getPits().get(0).getStoneCount());
		assertEquals(0, game.getPits().get(1).getStoneCount());
		assertEquals(8, game.getPits().get(2).getStoneCount());
		assertEquals(8, game.getPits().get(3).getStoneCount());
		assertEquals(8, game.getPits().get(4).getStoneCount());
		assertEquals(8, game.getPits().get(5).getStoneCount());
		assertEquals(2, game.getPits().get(6).getStoneCount());
		assertEquals(7, game.getPits().get(7).getStoneCount());

		assertNotEquals(currentPlayer, game.getPlayerTurn().getId());
	}

	@Test
	void sowFifthPitThatEndIntoFirstPitAndSwitchPlayerTurn() {
		Game game = gameService.createNewGame("p1", "p2");

		Long currentPlayer = game.getPlayerTurn().getId();

		gameService.sow(game.getPits(), 0);
		gameService.sow(game.getPits(), 1);
		gameService.sow(game.getPits(), 7);
		gameService.sow(game.getPits(), 5);

  		assertEquals(2, game.getPits().get(0).getStoneCount());
		assertEquals(0, game.getPits().get(1).getStoneCount());
		assertEquals(8, game.getPits().get(2).getStoneCount());
		assertEquals(8, game.getPits().get(3).getStoneCount());
		assertEquals(8, game.getPits().get(4).getStoneCount());
		assertEquals(0, game.getPits().get(5).getStoneCount());
		assertEquals(3, game.getPits().get(6).getStoneCount());
		assertEquals(1, game.getPits().get(7).getStoneCount());
		assertEquals(9, game.getPits().get(8).getStoneCount());
		assertEquals(8, game.getPits().get(9).getStoneCount());
		assertEquals(8, game.getPits().get(10).getStoneCount());
		assertEquals(8, game.getPits().get(11).getStoneCount());
		assertEquals(8, game.getPits().get(12).getStoneCount());
		assertEquals(1, game.getPits().get(13).getStoneCount());

		assertNotEquals(currentPlayer, game.getPlayerTurn().getId());
	}

}
