package io.nassar.game.mancala.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    Integer index;

    @ManyToOne
    Player player;

    @ManyToOne
    Game game;

    @Column(columnDefinition = "boolean default false")
    Boolean isBigPit = false;

    @Column(columnDefinition = "number default 0")
    Integer stoneCount = 0;

    public Pit(Integer index, Player player, Boolean isBigPit, Integer stoneCount) {
        this.index = index;
        this.player = player;
        this.game = game;
        this.isBigPit = isBigPit;
        this.stoneCount = stoneCount;
    }

    public Pit(Integer index, Player player, Integer stoneCount) {
        this(index, player, false, stoneCount);
    }
}