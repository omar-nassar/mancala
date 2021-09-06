package io.nassar.game.mancala.service;

import io.nassar.game.mancala.domain.Game;
import io.nassar.game.mancala.domain.Pit;
import io.nassar.game.mancala.domain.Player;
import io.nassar.game.mancala.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Log4j2
@Service
@RequiredArgsConstructor
public class PlayerService {

    public Player generatePlayerTurn(List<Player> players) {
        if(players != null && players.size() != 2) {
            log.error("Invalid players count ... ");
            players.stream().forEach(log::error);
            throw new RuntimeException("Invalid players count");
        }

        return new Random().nextBoolean() ? players.get(0) : players.get(1);
    }

    public List<Player> constructPlayers(String player1Name, String player2Name) {
        return Arrays.asList(   new Player(player1Name),
                                new Player(player2Name));
    }

    public void switchTurn(Game game, Pit currentPit) {
        if(currentPit.getIndex() <= PitService.BIG_PIT_1_INDEX) {
            currentPit.getGame().setPlayerTurn(game.getPits().get(PitService.BIG_PIT_2_INDEX).getPlayer());
        } else {
            currentPit.getGame().setPlayerTurn(game.getPits().get(PitService.BIG_PIT_1_INDEX).getPlayer());
        }
    }

    public void validatePlayerTurn(Long currentPitPlayerId, Long playerTurnId) {
        if(!currentPitPlayerId.equals(playerTurnId)) {
            throw new BusinessException("Invalid user turn!");
        }
    }
}