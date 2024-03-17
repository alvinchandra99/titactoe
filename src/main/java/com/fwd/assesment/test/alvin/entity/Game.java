package com.fwd.assesment.test.alvin.entity;


import com.fwd.assesment.test.alvin.model.GameMode;
import com.fwd.assesment.test.alvin.model.GameStatus;
import com.fwd.assesment.test.alvin.model.Player;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "game")
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "mode")
    @Enumerated(EnumType.STRING)
    private GameMode mode;

    @Column(name = "player")
    @Enumerated(EnumType.STRING)
    private Player currentPlayer;

    @Column(name = "winner")
    @Enumerated(EnumType.STRING)
    private Player winner;

    @OneToOne(cascade = CascadeType.ALL)
    private Board board;
}
