package io.nassar.game.mancala.dto.mapper;

import io.nassar.game.mancala.domain.Pit;
import io.nassar.game.mancala.dto.response.PitResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PitMapper {

    PitResponse toResponse(Pit pit);
}
