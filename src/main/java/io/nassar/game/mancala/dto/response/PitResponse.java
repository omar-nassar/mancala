package io.nassar.game.mancala.dto.response;

import lombok.Data;

@Data
public class PitResponse {
    Integer index;

    Boolean isBigPit;

    Integer stoneCount = 0;
}
