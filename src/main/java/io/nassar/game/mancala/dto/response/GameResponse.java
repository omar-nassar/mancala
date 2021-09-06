package io.nassar.game.mancala.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameResponse {
    Long id;

    Boolean hasGameFinished;

    PlayerResponse winnerPlayer;

    PlayerResponse playerTurn;

    List<PitResponse> pits;
}