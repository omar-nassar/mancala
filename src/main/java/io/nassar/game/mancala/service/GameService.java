package io.nassar.game.mancala.service;

import io.nassar.game.mancala.domain.Game;
import io.nassar.game.mancala.domain.Pit;
import io.nassar.game.mancala.exception.BusinessException;
import io.nassar.game.mancala.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
    final GameRepository gameRepository;
    final PitService pitService;
    final PlayerService playerService;

    /**
     * Creates a new game instance with six stones per each pit for both players.
     * Player turn will be randomly selected.
     *
     * @Param player1Name
     * @param player2Name
     *
     * @return {@link Game}
     *
     * */
    public Game createNewGame(String player1Name, String player2Name) {
        final Game game = new Game();

        game.addPlayers(playerService.constructPlayers(player1Name, player2Name));

        game.setPlayerTurn(playerService.selectPlayerTurn(game.getPlayers()));

        game.addPits(pitService.generateInitialPits(game.getPlayers().get(0),
                                                        game.getPlayers().get(1)));

        return gameRepository.save(game);
    }

    /**
     * Sow pit at index "{@code pitIndex}" for a specified "{@code gameId}".
     *
     * @param gameId
     * @param pitIndex pit to sow stones from
     *
     * @return {@link Game}
     *
     * */
    public Game sow(Long gameId, int pitIndex) {
        final Game game = gameRepository.findById(gameId)
                                        .orElseThrow(() -> new BusinessException("Invalid game ID: " + gameId));

        doValidations(game, pitIndex);

        sow(game, pitIndex);

        return gameRepository.save(game);
    }

    /**
     * Sow the stones of pit "{@code startingPitIndex}" for the game
     *
     * @param game
     * @param startingPitIndex
     *
     * */
    public void sow(Game game, int startingPitIndex) {
        int stonesInHand = game.getPits().get(startingPitIndex).getStoneCount();
        game.getPits().get(startingPitIndex).setStoneCount(0);

        sow(game, startingPitIndex, stonesInHand);

        if(hasGameFinished(game)) {
            finishTheGame(game);
        }
    }

    /**
     * Sow the stones in hand "{@code stonesInHand}" of index "{@code pitIndex}" for the passed game instance.
     *
     * @param game
     * @param pitIndex the index of the pit
     * @param stonesInHand the stones of the pit that the user wants to sow
     * */
    private void sow(Game game, int pitIndex, int stonesInHand) {
        int startingPitIndex = pitIndex;

        while(true) {
            Pit currentPit = game.getPits().get(++pitIndex);

            pitIndex = resetPitIndexIfReachedTheEnd(game, pitIndex);

            if(pitService.isOtherPlayerBigPit(currentPit, game.getPits().get(startingPitIndex))){
                continue;
            }

            currentPit.setStoneCount(currentPit.getStoneCount() + 1);
            stonesInHand--;

            if(stonesInHand == 0) {
                handleStonesInHandAreEmpty(game, startingPitIndex, currentPit);
                break;
            }
        }
    }

    private int resetPitIndexIfReachedTheEnd(Game game, int pitIndex) {
        if(pitIndex == (game.getPits().size()-1)) {
            pitIndex = -1;
        }

        return pitIndex;
    }

    /**
     * When stones in hand are empty, we have to check if the last pit was big or little one to determine the player turn.
     * Also, this checks if it's my little pit and contains only 1 pit in order to collect the opposite pit's stones.
     * {@link PitService#moveStonesFromCurrentAndOppositeIntoMyBigPit}
     *
     * @param game
     * @param startingPitIndex
     * @param currentPit
     * */
    private void handleStonesInHandAreEmpty(Game game, int startingPitIndex, Pit currentPit) {
        if(!currentPit.getIsBigPit()) {
            playerService.switchTurn(game, game.getPits().get(startingPitIndex));
        }

        if(pitService.isMyLittlePit(currentPit, game.getPits().get(startingPitIndex))) {
            if(currentPit.getStoneCount() == 1) {
                pitService.moveStonesFromCurrentAndOppositeIntoMyBigPit(game, currentPit);
            }
        }
    }

    private boolean hasGameFinished(Game game) {
        return  pitService.arePitsInRangeEmpty(game.getPits(), 0, PitService.BIG_PIT_1_INDEX) ||
                pitService.arePitsInRangeEmpty(game.getPits(), PitService.BIG_PIT_1_INDEX+1, PitService.BIG_PIT_2_INDEX);
    }

    private void finishTheGame(Game game) {
        game.setHasGameFinished(true);
        moveAllStonesIntoBigPits(game);
        determineGameWinner(game);
    }

    private void determineGameWinner(Game game) {
        final Pit firstBigPit = game.getPits().get(PitService.BIG_PIT_1_INDEX);
        final Pit secondBigPit = game.getPits().get(PitService.BIG_PIT_2_INDEX);

        game.setWinnerPlayer(firstBigPit.getStoneCount() > secondBigPit.getStoneCount()?
                                        firstBigPit.getPlayer() :
                                        secondBigPit.getPlayer());
    }

    public void moveAllStonesIntoBigPits(Game game) {
        pitService.moveAllStonesToBigPit(game.getPits(), 0, PitService.BIG_PIT_1_INDEX);
        pitService.moveAllStonesToBigPit(game.getPits(), PitService.BIG_PIT_1_INDEX+1, PitService.BIG_PIT_2_INDEX);
    }

    private void doValidations(Game game, int pitIndex) {
        validateIfGameFinished(game);
        playerService.validatePlayerTurn(game.getPits().get(pitIndex).getPlayer().getId(), game.getPlayerTurn().getId());
        pitService.doValidations(game, pitIndex);
    }

    private void validateIfGameFinished(Game game) {
        if(game.getHasGameFinished()) {
            throw new BusinessException("Game already finished!");
        }
    }
}