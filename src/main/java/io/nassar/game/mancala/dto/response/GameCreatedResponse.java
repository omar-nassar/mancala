package io.nassar.game.mancala.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GameCreatedResponse {
    Long id;
    PlayerResponse playerTurn;

    List<PlayerResponse> players;

    List<PitResponse> pits;
}
