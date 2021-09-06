package io.nassar.game.mancala.controller;

import io.nassar.game.mancala.dto.mapper.GameMapper;
import io.nassar.game.mancala.dto.request.NewGameRequest;
import io.nassar.game.mancala.dto.response.GameCreatedResponse;
import io.nassar.game.mancala.dto.response.GameResponse;
import io.nassar.game.mancala.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("game")
@RequiredArgsConstructor
public class GameController {

    final GameService gameService;

    @Autowired
    GameMapper gameMapper;

    @PostMapping
    public GameCreatedResponse createNewGame(@RequestBody NewGameRequest newGameRequest) {
        return gameMapper.toGameCreatedResponse(gameService.createNewGame(  newGameRequest.getPlayer1Name(),
                                                                            newGameRequest.getPlayer2Name()));
    }

    @PutMapping("/{gameId}/pit/{index}")
    public GameResponse sow(@PathVariable Long gameId, @PathVariable Integer index) {
        return gameMapper.toGameResponse(gameService.sow(gameId, index));
    }
}
