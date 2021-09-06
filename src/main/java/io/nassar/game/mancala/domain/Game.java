package io.nassar.game.mancala.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(columnDefinition = "boolean default false")
    Boolean hasGameFinished = false;

    @OneToOne
    Player winnerPlayer;

    LocalDateTime createdAt;

    @OneToOne
    Player playerTurn;

    @OneToMany( cascade = CascadeType.ALL,
                mappedBy = "game")
    List<Player> players = new ArrayList<>();

    @OneToMany( cascade = CascadeType.ALL,
                mappedBy = "game",
                fetch = FetchType.EAGER)
    List<Pit> pits = new ArrayList<>();

    public void addPit(Pit pit) {
        this.pits.add(pit);
        pit.setGame(this);
    }

    public void addPits(List<Pit> initialGamePits) {
        initialGamePits.stream().forEach(this::addPit);
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        player.setGame(this);
    }

    public void addPlayers(List<Player> players) {
        players.stream().forEach(this::addPlayer);
    }

    @PrePersist
    public void prePersist() {
        this.setCreatedAt(LocalDateTime.now());
    }
}