package com.fwd.assesment.test.alvin.controller;

import com.fwd.assesment.test.alvin.model.common.CommonResponse;
import com.fwd.assesment.test.alvin.model.request.MoveRequest;
import com.fwd.assesment.test.alvin.model.request.StartGameRequest;
import com.fwd.assesment.test.alvin.model.response.GameResponse;
import com.fwd.assesment.test.alvin.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController("/api/game")
@AllArgsConstructor
public class GameController {

  private GameService gameService;

  @PostMapping(value = "/start")
  public CommonResponse<GameResponse> startGame(@RequestBody StartGameRequest request) {
    CommonResponse<GameResponse> response = new CommonResponse<>();

    GameResponse gameResponse = gameService.startGame(request);
    response.setCode(200);
    response.setStatus(HttpStatus.OK.toString());
    response.setData(gameResponse);

    return response;
  }

  @GetMapping("/grid/{gameId}")
  public String getPrettierGrid(@PathVariable("gameId") String gameId) {
    return gameService.getPrettierGrid(gameId);
  }

  @PostMapping("/move")
  public CommonResponse<GameResponse> makeMove(@RequestBody MoveRequest request) {
    GameResponse gameResponse = gameService.makeMove(request);

    CommonResponse<GameResponse> response = new CommonResponse<>();
    response.setData(gameResponse);
    response.setCode(200);
    response.setStatus(HttpStatus.OK.toString());

    return response;
  }
}
