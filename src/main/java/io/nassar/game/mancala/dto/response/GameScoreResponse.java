package io.nassar.game.mancala.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameScoreResponse {
    Long id;

    Boolean isTie;

    PlayerScoreResponse player1;
    PlayerScoreResponse player2;

}
