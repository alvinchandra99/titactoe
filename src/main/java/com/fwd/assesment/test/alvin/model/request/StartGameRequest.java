package com.fwd.assesment.test.alvin.model.request;

import com.fwd.assesment.test.alvin.model.GameMode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StartGameRequest {
  @Min(3)
  private int boardSize;

  @NotNull
  private GameMode gameMode;
}
