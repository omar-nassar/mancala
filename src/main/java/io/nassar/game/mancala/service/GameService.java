package io.nassar.game.mancala.service;

import io.nassar.game.mancala.domain.Game;
import io.nassar.game.mancala.domain.Pit;
import io.nassar.game.mancala.domain.Player;
import io.nassar.game.mancala.exception.BusinessException;
import io.nassar.game.mancala.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {
    final GameRepository gameRepository;
    final PitService pitService;
    final PlayerService playerService;

    public Game createNewGame(String player1Name, String player2Name) {
        final Game game = new Game();

        game.setPlayers(playerService.constructPlayers(player1Name, player2Name));

        game.setPlayerTurn(playerService.generatePlayerTurn(game.getPlayers()));

        game.addPits(pitService.generateInitialGamePits(game.getPlayers().get(0),
                                                        game.getPlayers().get(1)));

        return gameRepository.save(game);
    }

    public void move(Long gameId, Integer pitIndex) {
        final Game game = gameRepository.findById(gameId)
                                        .orElseThrow(() -> new BusinessException("Invalid game ID" + gameId));

        game.setPits(game.getPits().stream().sorted(Comparator.comparingInt(Pit::getIndex)).collect(Collectors.toList()));

        doValidations(game, pitIndex);

        sow(game.getPits(), pitIndex);
    }

    public void sow(List<Pit> pits, Integer startingPitIndex) {
        final Pit pit = pits.get(startingPitIndex);
        Integer stoneCount = pit.getStoneCount();
        pit.setStoneCount(0);

        int pointer = pit.getIndex();
        Pit myBigPit = null;

        while(true) {
            Pit currentPit = pits.get(++pointer);

            if(pointer == (pits.size()-1)) {
                pointer = -1;
            }

            final boolean isMyPit = isMyPit(currentPit, pit.getPlayer());

            if(currentPit.getIsBigPit() && !isMyPit){
                continue;
            }

            if(currentPit.getIsBigPit() && isMyPit) {
                myBigPit = currentPit;
                currentPit.setStoneCount(currentPit.getStoneCount() + 1);
            } else {
                currentPit.setStoneCount(currentPit.getStoneCount() + 1);
            }
            stoneCount--;

            if(stoneCount == 0) {
                if(!currentPit.getIsBigPit()) {
                    switchPlayerTurn(pits, pits.get(startingPitIndex));
                }
                if(!currentPit.getIsBigPit() && isMyPit) {
                    if(currentPit.getStoneCount() == 1) {
                        Pit opponentOppositePit = getOppositePit(currentPit, pits);
                        Integer oppositePitStoneCount = opponentOppositePit.getStoneCount();
                        opponentOppositePit.setStoneCount(0);

                        if(myBigPit == null) {
                            myBigPit = getMyBigPit(pits, currentPit);
                        }

                        myBigPit.setStoneCount( myBigPit.getStoneCount() +
                                                oppositePitStoneCount +
                                                1);

                        currentPit.setStoneCount(0);
                    }
                }
                break;
            }

            //check if game finishes
        }

        if( checkIfGameFinished(pits, PitService.BIG_PIT_1_INDEX) ||
            checkIfGameFinished(pits, PitService.BIG_PIT_2_INDEX)) {
            pit.getGame().setHasGameFinished(true);
            final Pit firstBigPit = pits.get(PitService.BIG_PIT_1_INDEX);
            final Pit secondBigPit = pits.get(PitService.BIG_PIT_2_INDEX);

            pit.getGame().setWinnerPlayer(firstBigPit.getStoneCount() > secondBigPit.getStoneCount()?
                                            firstBigPit.getPlayer() :
                                            firstBigPit.getPlayer());
        }
    }

    private boolean checkIfGameFinished(List<Pit> pits, int bigPitIndex) {
        boolean isAllEmpty = true;
        for (int i = 0; i < bigPitIndex; i++) {
            if(pits.get(i).getStoneCount() != 0)  {
                isAllEmpty = false;
                break;
            }
        }

        if(isAllEmpty) {
            return true;
        }

        return false;
    }

    private void switchPlayerTurn(List<Pit> pits, Pit currentPit) {
        if(currentPit.getIndex() < PitService.BIG_PIT_1_INDEX) {
            currentPit.getGame().setPlayerTurn(pits.get(PitService.BIG_PIT_2_INDEX).getPlayer());
        } else {
            currentPit.getGame().setPlayerTurn(pits.get(PitService.BIG_PIT_1_INDEX).getPlayer());
        }
    }

    private Pit getMyBigPit(List<Pit> pits, Pit currentPit) {
        return currentPit.getIndex() < PitService.BIG_PIT_1_INDEX?  pits.get(PitService.BIG_PIT_1_INDEX):
                                                                    pits.get(PitService.BIG_PIT_2_INDEX);
    }

    private Pit getOppositePit(Pit myPit, List<Pit> allPits) {
        return allPits.get(myPit.getIndex() + PitService.SINGLE_PLAYER_NORMAL_PIT_COUNT);
    }

    private boolean isMyPit(Pit nextPit, Player player) {
        return nextPit.getPlayer().getId().equals(player.getId());
    }

    private void doValidations(Game game, Integer pitIndex) {
        validateIfGameFinished(game);
        validatePlayerTurn(game, pitIndex);
        validateIsNotBigPit(game, pitIndex);
        validatePitSize(game, pitIndex);
    }

    private void validateIfGameFinished(Game game) {
        if(game.getHasGameFinished()) {
            throw new BusinessException("Game already finished!");
        }
    }

    private void validateIsNotBigPit(Game game, Integer pitIndex) {
        if(game.getPits().get(pitIndex).getIsBigPit()) {
            throw new BusinessException("Moving stones from big pit is not allowed!");
        }
    }

    private void validatePitSize(Game game, Integer pitIndex) {
        if(game.getPits().get(pitIndex).getStoneCount().equals(0)) {
            throw new BusinessException("Pit is empty!");
        }
    }

    private void validatePlayerTurn(Game game, Integer pitIndex) {
        final boolean isValidUserTurn = game.getPits()
                                            .get(pitIndex)
                                            .getPlayer()
                                            .getId().equals(game.getPlayerTurn().getId());

        if(!isValidUserTurn) {
            throw new BusinessException("Invalid user turn!");
        }
    }
/*
    private Pit getPit(List<Pit> pits, Integer pitIndex) {
        return pits.get(pitIndex - 1);
    }*/
}