package com.fwd.assesment.test.alvin.service.impl;

import com.fwd.assesment.test.alvin.entity.Board;
import com.fwd.assesment.test.alvin.entity.Game;
import com.fwd.assesment.test.alvin.helper.GameHelper;
import com.fwd.assesment.test.alvin.model.GameMode;
import com.fwd.assesment.test.alvin.model.GameStatus;
import com.fwd.assesment.test.alvin.model.Player;
import com.fwd.assesment.test.alvin.model.exception.BadRequestException;
import com.fwd.assesment.test.alvin.model.exception.ServerErrorException;
import com.fwd.assesment.test.alvin.model.request.MoveRequest;
import com.fwd.assesment.test.alvin.model.request.StartGameRequest;
import com.fwd.assesment.test.alvin.model.response.GameResponse;
import com.fwd.assesment.test.alvin.repository.GameRepository;
import com.fwd.assesment.test.alvin.service.GameService;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;

@Component
@AllArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

  private GameRepository gameRepository;
  private ChatClient chatClient;

  public GameResponse startGame(StartGameRequest request) {
    Board board = new Board(request.getBoardSize());

    Game game = new Game();
    game.setBoard(board);
    game.setStatus(GameStatus.START);
    game.setMode(request.getGameMode());
    game = gameRepository.save(game);

    return constructGameResponse(game);
  }

  @Transactional
  public GameResponse makeMove(MoveRequest request) {
    Game game = gameRepository.findById(request.getGameId()).orElseGet(() -> null);
    validateMoveRequest(request, game);
    updateBoardAndGameState(game, request);

    if (game.getMode().equals(GameMode.PLAYER_VS_AI)) {
      processAIMove(game);
    }
    gameRepository.save(game);
    return constructGameResponse(game);
  }

  private void updateBoardAndGameState(Game game, MoveRequest request) {
    Board board = game.getBoard();
    Player[][] grid = board.getGrid();
    grid[request.getRow()][request.getCol()] = request.getPlayer();
    board.setGrid(grid);

    game.setCurrentPlayer(request.getPlayer());
    game.setStatus(GameStatus.IN_PROGRESS);

    Player winner = GameHelper.findWinnerByCurrentMovement(request.getRow(),
            request.getCol(),
            board.getGrid(), 3);
    if (winner != null) {
      game.setWinner(winner);
      game.setStatus(GameStatus.HAS_WINNER);
    } else if (GameHelper.isGridAlreadyFull(board.getGrid())) {
      game.setStatus(GameStatus.DRAW);
    }
  }

  private void processAIMove(Game game) {
    if (game.getWinner() != null) {
      return;
    }
    Player aiRole = game.getCurrentPlayer() == Player.PLAYER_X ? Player.PLAYER_O : Player.PLAYER_X;
    MoveRequest aiMovementRequest =
        constructAIMovementRequest(aiRole, game.getBoard().getGrid(), game.getId());
    updateBoardAndGameState(game, aiMovementRequest);
  }

  private MoveRequest constructAIMovementRequest(Player aiRole, Player[][] grid, String gameId) {
    String aiStrategyPrompt =
        String.format(
            """
    You are a master Tic-Tac-Toe strategist, Unbeatable tic tac toe player. Analyze the board and recommend the best possible move for %s to maximize their chances of winning. Follow these principles:
    * **Immediate Wins:** If you can win on this turn, take that move.
    * **Block Opponent Wins:** If the opponent can win on their next turn, block them.
    * **Create Forks:** Try to set up situations where you have multiple winning threats at once.\s
    * **Center Control:** If possible, prioritize the center square.
    * **Corner Strategy:** Aim to occupy corner squares.
    * **Expected JSON format:**
    The current board state is %s. Provide the move in the json string format, no explanation neede {"row": {value}, "col": {value}} (zero-indexed).
    """,
            aiRole.toString(), Arrays.deepToString(grid));

    MoveRequest aiMovementRequest = null;
    try {
      String response = chatClient.call(aiStrategyPrompt);
      log.info(
          "Calling openAiClient with prompt : {}  and reponse {} ", aiStrategyPrompt, response);
      Gson gson = new Gson();
      aiMovementRequest = gson.fromJson(response, MoveRequest.class);
      aiMovementRequest.setGameId(gameId);
      aiMovementRequest.setPlayer(aiRole);
    } catch (Exception e) {
      throw new ServerErrorException();
    }

    return aiMovementRequest;
  }

  private void validateMoveRequest(MoveRequest request, Game game) {
    if (game == null) {
      throw new BadRequestException("Game Not Found");
    }
    if (game.getStatus() != GameStatus.IN_PROGRESS && game.getStatus() != GameStatus.START) {
      throw new BadRequestException("Game is over!");
    }
    if (request.getPlayer() == game.getCurrentPlayer()) {
      throw new BadRequestException("It's your opponent turn!");
    }
    if (request.getCol() > game.getBoard().getSize()
        || request.getRow() > game.getBoard().getSize()) {
      throw new BadRequestException("Movement is out of the box!");
    }
    if (game.getBoard().getGrid()[request.getRow()][request.getCol()] != null) {
      throw new BadRequestException("Movement is already exist");
    }
  }

  public String getPrettierGrid(String gameId) {
    Game game = gameRepository.findById(gameId).orElseGet(() -> null);
    if (game == null) {
      throw new BadRequestException("Game Not Found");
    }

    return GameHelper.getPrettyGrid(game.getBoard().getGrid());
  }

  private GameResponse constructGameResponse(Game game) {
    GameResponse gameResponse = new GameResponse();
    gameResponse.setId(game.getId());
    gameResponse.setStatus(game.getStatus());
    gameResponse.setGrid(game.getBoard().getGrid());
    gameResponse.setMode(game.getMode());
    gameResponse.setWinner(game.getWinner());
    return gameResponse;
  }
}
