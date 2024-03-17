package com.fwd.assesment.test.alvin.model.response;

import com.fwd.assesment.test.alvin.model.GameMode;
import com.fwd.assesment.test.alvin.model.GameStatus;
import com.fwd.assesment.test.alvin.model.Player;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GameResponse {
  private String id;

  private GameStatus status;

  private GameMode mode;

  private Player winner;

  private Player[][] grid;
}
