package io.nassar.game.mancala.dto.mapper;

import io.nassar.game.mancala.domain.Game;
import io.nassar.game.mancala.dto.response.GameCreatedResponse;
import io.nassar.game.mancala.dto.response.GameResponse;
import io.nassar.game.mancala.dto.response.GameScoreResponse;
import io.nassar.game.mancala.dto.response.PlayerScoreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { PitMapper.class, PlayerMapper.class } )
public interface GameMapper {

    GameCreatedResponse toGameCreatedResponse(Game game);

    GameResponse toGameResponse(Game game);

    @Mapping(target = "playerTurn", ignore = true)
    @Mapping(target = "pits", ignore = true)
    GameResponse toGameFinishedResponse(Game game);

    @Mapping(source = "game.id", target = "id")
    @Mapping(source = "game.isTie", target = "isTie")
    @Mapping(source = "player1", target = "player1")
    @Mapping(source = "player2", target = "player2")
    GameScoreResponse toGameScoreResponse(Game game, PlayerScoreResponse player1, PlayerScoreResponse player2);
}