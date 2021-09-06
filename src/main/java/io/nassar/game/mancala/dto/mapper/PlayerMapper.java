package io.nassar.game.mancala.dto.mapper;

import io.nassar.game.mancala.domain.Player;
import io.nassar.game.mancala.dto.response.PlayerResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    PlayerResponse toResponse(Player player);
}
