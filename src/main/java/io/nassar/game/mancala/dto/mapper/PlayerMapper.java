package io.nassar.game.mancala.dto.mapper;

import io.nassar.game.mancala.domain.Player;
import io.nassar.game.mancala.dto.response.PlayerResponse;
import io.nassar.game.mancala.dto.response.PlayerScoreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    PlayerResponse toResponse(Player player);

    @Mapping(source = "score", target = "score")
    PlayerScoreResponse toPlayerScoreResponse(Player player, int score);

    @Mapping(source = "score", target = "score")
    @Mapping(source = "isWinner", target = "isWinner")
    PlayerScoreResponse toPlayerScoreResponse(Player player, int score, Boolean isWinner);
}
