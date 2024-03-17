package com.fwd.assesment.test.alvin.model.request;


import com.fwd.assesment.test.alvin.model.Player;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MoveRequest {
    @NotEmpty
    private String gameId;
    @Min(value = 0)
    private int col;
    @Min(value = 0)
    private int row;
    @NotNull
    private Player player;
}
