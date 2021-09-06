package io.nassar.game.mancala.controller;

import io.nassar.game.mancala.dto.mapper.GameMapper;
import io.nassar.game.mancala.dto.request.NewGameRequest;
import io.nassar.game.mancala.dto.response.GameCreatedResponse;
import io.nassar.game.mancala.dto.response.GameResponse;
import io.nassar.game.mancala.service.GameService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
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
        return gameMapper.toGameResponse(gameService.sow(gameId, index));
    }
}