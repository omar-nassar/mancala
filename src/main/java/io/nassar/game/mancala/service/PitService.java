package io.nassar.game.mancala.service;

import io.nassar.game.mancala.domain.Game;
import io.nassar.game.mancala.domain.Pit;
import io.nassar.game.mancala.domain.Player;
import io.nassar.game.mancala.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PitService {
    public static final int DEFAULT_BIG_PIT_STONE_COUNT = 0;
    public static final int DEFAULT_PIT_STONE_COUNT = 6;
    public static final int BIG_PIT_1_INDEX = 6;
    public static final int BIG_PIT_2_INDEX = 13;

    /**
     * Prepares list of pits each initialized with six stones. Linking each pit with the corresponding player.
     *
     * @param player1
     * @param player2
     *
     * @return {@link List<Pit>}
     *
     * */
    public List<Pit> generateInitialPits(Player player1, Player player2) {

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

    public Pit getMyBigPit(List<Pit> pits, Pit currentPit) {
        return currentPit.getIndex() < PitService.BIG_PIT_1_INDEX?  pits.get(PitService.BIG_PIT_1_INDEX):
                                                                    pits.get(PitService.BIG_PIT_2_INDEX);
    }

    /**
     * Determins the opposite pit of "{@code myPit}"
     *
     * @param allPits
     * @param myPit
     *
     * @return {@link Pit} the opposite pit
     * */
    public Pit getOppositePit(List<Pit> allPits, Pit myPit) {
        if(myPit.getIndex() < PitService.BIG_PIT_1_INDEX) {
            return allPits.get(allPits.size() - (myPit.getIndex() + 2));
        } else {
            return allPits.get(allPits.size() - myPit.getIndex() - 2);
        }
    }

    public boolean isMyPit(Pit currentPit, Pit pitToSowFrom) {
        return currentPit.getPlayer().getId().equals(pitToSowFrom.getPlayer().getId());
    }

    public boolean isMyLittlePit(Pit currentPit, Pit pitToSowFrom) {
        return !currentPit.getIsBigPit() && isMyPit(currentPit, pitToSowFrom);
    }

    public boolean isOtherPlayerBigPit(Pit currentPit, Pit pitToSowFrom) {
        return currentPit.getIsBigPit() && !isMyPit(currentPit, pitToSowFrom);
    }

    public void moveAllStonesToBigPit(List<Pit> pits, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            if(pits.get(i).getStoneCount() != 0)  {
                pits.get(endIndex)
                    .setStoneCount(pits.get(endIndex).getStoneCount() + pits.get(i).getStoneCount());

                pits.get(i).setStoneCount(0);
            }
        }
    }

    /**
     * When last stone in hand lands into my empty and little pit,
     * this method will move this only stone and the stones inside the opposite pit into my big pit.
     *
     * @param game
     * @param currentPit
     *
     * */
    public void moveStonesFromCurrentAndOppositeIntoMyBigPit(Game game, Pit currentPit) {
        Pit oppositePit = getOppositePit(game.getPits(), currentPit);
        int oppositePitStoneCount = oppositePit.getStoneCount();
        oppositePit.setStoneCount(0);

        getMyBigPit(game.getPits(), currentPit).setStoneCount(  getMyBigPit(game.getPits(), currentPit).getStoneCount() +
                                                                oppositePitStoneCount +
                                                                currentPit.getStoneCount());

        currentPit.setStoneCount(0);
    }

    public boolean arePitsInRangeEmpty(List<Pit> pits, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            if(pits.get(i).getStoneCount() != 0)  {
                return false;
            }
        }

        return true;
    }

    public void doValidations(Game game, int pitIndex) {
        validateIsNotBigPit(game, pitIndex);
        validatePitSize(game, pitIndex);
    }

    public void validateIsNotBigPit(Game game, int pitIndex) {
        if(game.getPits().get(pitIndex).getIsBigPit()) {
            throw new BusinessException("Moving stones from big pit is not allowed!");
        }
    }

    public void validatePitSize(Game game, int pitIndex) {
        if(game.getPits().get(pitIndex).getStoneCount().equals(0)) {
            throw new BusinessException("Pit is empty!");
        }
    }
}