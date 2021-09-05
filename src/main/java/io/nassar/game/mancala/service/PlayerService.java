package io.nassar.game.mancala.service;

import io.nassar.game.mancala.domain.Player;
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

    private Player constructPlayer(String playerName) {
        final Player player = new Player();
        player.setName(playerName);
        return player;
    }

    public List<Player> constructPlayers(String player1Name, String player2Name) {
        return Arrays.asList(   constructPlayer(player1Name),
                                constructPlayer(player2Name));
    }
}