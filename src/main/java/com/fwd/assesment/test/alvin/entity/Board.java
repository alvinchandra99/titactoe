package com.fwd.assesment.test.alvin.entity;

import com.fwd.assesment.test.alvin.model.Player;
import io.hypersistence.utils.hibernate.type.array.EnumArrayType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.io.*;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "game")
@Getter
@Setter
public class Board {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Lob
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private byte[] byteGrid;

  @Column(name = "size")
  private int size;

  @OneToOne
  private Game game;

  public Board(int size) {
    this.size = size;
    setGrid(new Player[size][size]);
  }

  public Player[][] getGrid() {
    if (byteGrid == null) {
      return null;
    }
    try (ByteArrayInputStream bis = new ByteArrayInputStream(byteGrid);
        ObjectInputStream ois = new ObjectInputStream(bis)) {
      return (Player[][]) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      return null;
    }
  }

  public void setGrid(Player[][] grid) {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos)) {
      oos.writeObject(grid);
      byteGrid = bos.toByteArray();
    } catch (IOException e) {
      // Handle exceptions (e.g., log error, throw custom exception)
    }
  }

  public String getStringGrid() {
    StringBuilder builder = new StringBuilder();
    for (int row = 0; row < getGrid().length; row++) {
      for (int col = 0; col < getGrid()[row].length; col++) {
        if (getGrid()[row][col] == Player.PLAYER_X) {
          builder.append("X");
        } else if (getGrid()[row][col] == Player.PLAYER_O) {
          builder.append("O");
        } else {
          builder.append(" "); // Empty cell
        }
      }
      builder.append("\n"); // Newline after each row
    }
    return builder.toString();
  }
}
