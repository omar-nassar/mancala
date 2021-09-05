package io.nassar.game.mancala.service;

import io.nassar.game.mancala.domain.Pit;
import io.nassar.game.mancala.domain.Player;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PitService {
    public static final int DEFAULT_BIG_PIT_STONE_COUNT = 0;
    public static final int DEFAULT_PIT_STONE_COUNT = 6;
    public static final int SINGLE_PLAYER_NORMAL_PIT_COUNT = 6;
    public static final int BIG_PIT_1_INDEX = 6;
    public static final int BIG_PIT_2_INDEX = 13;

    public List<Pit> generateInitialGamePits(Player player1, Player player2) {

        final Pit pit1 = new Pit(0, player1, PitService.DEFAULT_PIT_STONE_COUNT);
        final Pit pit2 = new Pit(1, player1, PitService.DEFAULT_PIT_STONE_COUNT);
        final Pit pit3 = new Pit(2, player1, PitService.DEFAULT_PIT_STONE_COUNT);
        final Pit pit4 = new Pit(3, player1, PitService.DEFAULT_PIT_STONE_COUNT);
        final Pit pit5 = new Pit(4, player1, PitService.DEFAULT_PIT_STONE_COUNT);
        final Pit pit6 = new Pit(5, player1, PitService.DEFAULT_PIT_STONE_COUNT);
        final Pit pit7 = new Pit(BIG_PIT_1_INDEX, player1, true, PitService.DEFAULT_BIG_PIT_STONE_COUNT);

        final Pit pit8 = new Pit(7, player2, PitService.DEFAULT_PIT_STONE_COUNT);
        final Pit pit9 = new Pit(8, player2, PitService.DEFAULT_PIT_STONE_COUNT);
        final Pit pit10 = new Pit(9, player2, PitService.DEFAULT_PIT_STONE_COUNT);
        final Pit pit11 = new Pit(10, player2, PitService.DEFAULT_PIT_STONE_COUNT);
        final Pit pit12 = new Pit(11, player2, PitService.DEFAULT_PIT_STONE_COUNT);
        final Pit pit13 = new Pit(12, player2, PitService.DEFAULT_PIT_STONE_COUNT);
        final Pit pit14 = new Pit(BIG_PIT_2_INDEX, player2, true, PitService.DEFAULT_BIG_PIT_STONE_COUNT);

        return Arrays.asList(   pit1, pit2, pit3, pit4, pit5, pit6, pit7,
                                pit8, pit9, pit10, pit11, pit12, pit13, pit14);
    }
}