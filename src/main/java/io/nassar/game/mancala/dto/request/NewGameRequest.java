package io.nassar.game.mancala.dto.request;

import lombok.Data;

@Data
public class NewGameRequest {
    String player1Name;
    String player2Name;
}
