package io.nassar.game.mancala.controller;

import io.nassar.game.mancala.domain.Game;
import io.nassar.game.mancala.domain.Player;
import io.nassar.game.mancala.dto.mapper.GameMapper;
import io.nassar.game.mancala.dto.mapper.PlayerMapper;
import io.nassar.game.mancala.dto.request.NewGameRequest;
import io.nassar.game.mancala.dto.response.GameCreatedResponse;
import io.nassar.game.mancala.dto.response.GameResponse;
import io.nassar.game.mancala.dto.response.GameScoreResponse;
import io.nassar.game.mancala.service.GameService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("game")
@RequiredArgsConstructor
public class GameController {

    final GameService gameService;

    @Autowired
    GameMapper gameMapper;

    @Autowired
    PlayerMapper playerMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(   consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(  response = GameCreatedResponse.class,
                    value = "A new game will be created with this API. By default each pit will contain six stones. " +
                            "You have to pass the player names. " +
                            "The response will contain the player's turn so he/she can sow first!")
    public GameCreatedResponse createNewGame(@RequestBody NewGameRequest newGameRequest) {
        return gameMapper.toGameCreatedResponse(gameService.createNewGame(  newGameRequest.getPlayer1Name(),
                                                                            newGameRequest.getPlayer2Name()));
    }

    @PutMapping(value = "/{gameId}/pit/{index}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(  response = GameResponse.class,
                    value = "To sow stones from a pit. You have to pass the game ID and the pit's index.")
    public GameResponse sow(@PathVariable Long gameId, @PathVariable Integer index) {
        final Game game = gameService.sow(gameId, index);

        if(!game.getHasGameFinished()) {
            return gameMapper.toGameResponse(game);
        } else {
            return gameMapper.toGameFinishedResponse(game);
        }
    }

    @GetMapping(value = "score/{gameId}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(  response = GameScoreResponse.class,
                    value = "To retrieve the score of the game when it finished")
    public GameScoreResponse getGameScore(@PathVariable Long gameId) {

        Game game = gameService.getGame(gameId);

        if(game.getIsTie()) {
            return getTieGameResponse(game);
        }

        return getGameScoreResponse(game);
    }

    private GameScoreResponse getGameScoreResponse(Game game) {
        Player looserPlayer = gameService.getLooser(game);

        int winnerScore = gameService.getPlayerScore(game.getWinnerPlayer());
        int looserScore = gameService.getPlayerScore(looserPlayer);

        return gameMapper.toGameScoreResponse(  game,
                                                playerMapper.toPlayerScoreResponse(game.getWinnerPlayer(), winnerScore, true),
                                                playerMapper.toPlayerScoreResponse(looserPlayer, looserScore));
    }

    private GameScoreResponse getTieGameResponse(Game game) {
        int bigPitStoneCount = gameService.getPlayerScore(game.getPlayers().get(0));

        return gameMapper.toGameScoreResponse(  game,
                                                playerMapper.toPlayerScoreResponse(game.getPlayers().get(0), bigPitStoneCount),
                                                playerMapper.toPlayerScoreResponse(game.getPlayers().get(1), bigPitStoneCount));
    }
}