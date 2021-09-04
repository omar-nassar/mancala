package io.nassar.game.mancala.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    @OneToMany(mappedBy = "player")
    List<Pit> pits = new ArrayList<>();

    @ManyToOne
    Game game;

    @Override
    public String toString() {
        return  "Player{" +
                "id=" + id +
                ", name='" + name +
                ", game ID=" + game.getId() +
                '}';
    }
}
