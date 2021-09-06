package io.nassar.game.mancala.dto.mapper;

import io.nassar.game.mancala.domain.Game;
import io.nassar.game.mancala.dto.response.GameCreatedResponse;
import io.nassar.game.mancala.dto.response.GameResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { PitMapper.class, PlayerMapper.class } )
public interface GameMapper {

    GameCreatedResponse toGameCreatedResponse(Game game);
    GameResponse toGameResponse(Game game);
}