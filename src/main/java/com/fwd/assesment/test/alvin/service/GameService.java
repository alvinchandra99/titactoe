package com.fwd.assesment.test.alvin.service;

import com.fwd.assesment.test.alvin.model.request.MoveRequest;
import com.fwd.assesment.test.alvin.model.request.StartGameRequest;
import com.fwd.assesment.test.alvin.model.response.GameResponse;

public interface GameService {
  GameResponse startGame(StartGameRequest request);

  GameResponse makeMove(MoveRequest request);

  String getPrettierGrid(String gameId);
}
