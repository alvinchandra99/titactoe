package com.fwd.assesment.test.alvin.helper;

import com.fwd.assesment.test.alvin.model.Player;

public class GameHelper {
  
  public static Player findWinnerByCurrentMovement(
      int currentRow, int currentCol, Player[][] grid, int minConsecutiveToWin) {
    Player player = grid[currentRow][currentCol];
    int gridSize = grid.length;

    if (checkRow(player, currentRow, grid, minConsecutiveToWin, gridSize))
      return player;

    if (checkColumn(player, currentCol, grid, minConsecutiveToWin, gridSize))
      return player;

    if (checkDiagonal(player, currentRow, currentCol, grid, minConsecutiveToWin, gridSize))
      return player;

    if (checkAntiDiagonal(currentRow, currentCol, grid, minConsecutiveToWin, gridSize, player))
      return player;

    return null;
  }

  public static boolean isGridAlreadyFull(Player[][] grid) {
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; i < grid.length; j++) {
        if (grid[i][j] == null) {
          return false;
        }
      }
    }

    return true;
  }

  public static String getPrettyGrid(Player[][] grid) {
    StringBuilder builder = new StringBuilder();

    String rowSeparator = getRowSeparator(grid.length);

    builder.append("  ");
    builder.append(rowSeparator);

    for (int row = 0; row < grid.length; row++) {
      builder.append(row + 1).append(" |"); // Add row numbers

      for (int col = 0; col < grid[0].length; col++) {
        String symbol = " "; // Default empty space
        if (grid[row][col] == Player.PLAYER_X) {
          symbol = "X";
        } else if (grid[row][col] == Player.PLAYER_O) {
          symbol = "O";
        }
        builder.append(" ").append(symbol).append(" |");
      }

      builder.append("\n  ");
      builder.append(rowSeparator); // Add row separators
    }

    return builder.toString();
  }

  private static String getRowSeparator(int gridSize){
    StringBuilder builder = new StringBuilder();
    for(int i = 0; i < gridSize; i++ ){
     builder.append("+---");
    }
    builder.append("+\n");
    return builder.toString();
  }

  private static boolean checkAntiDiagonal(
      int currentRow,
      int currentCol,
      Player[][] grid,
      int minConsecutiveToWin,
      int gridSize,
      Player player) {
    int consecutiveCount;
    if (currentRow + currentCol == gridSize - 1) {
      consecutiveCount = 0;
      for (int i = 0; i < gridSize; i++) {
        if (grid[i][(gridSize - 1) - i] == player) {
          consecutiveCount++;
          if (consecutiveCount >= minConsecutiveToWin) {
            return true;
          }
        } else {
          consecutiveCount = 0;
        }
      }
    }
    return false;
  }

  private static boolean checkDiagonal(
      Player player,
      int currentRow,
      int currentCol,
      Player[][] grid,
      int minConsecutiveToWin,
      int gridSize) {
    int consecutiveCount;
    if (currentRow == currentCol) {
      consecutiveCount = 0;
      for (int i = 0; i < gridSize; i++) {
        if (grid[i][i] == player) {
          consecutiveCount++;
          if (consecutiveCount >= minConsecutiveToWin) {
            return true;
          }
        } else {
          consecutiveCount = 0;
        }
      }
    }
    return false;
  }

  private static boolean checkColumn(
      Player player, int currentCol, Player[][] grid, int minConsecutiveToWin, int gridSize) {
    int consecutiveCount;
    consecutiveCount = 0;
    for (int row = 0; row < gridSize; row++) {
      if (grid[row][currentCol] == player) {
        consecutiveCount++;
        if (consecutiveCount >= minConsecutiveToWin) {
          return true;
        }
      } else {
        consecutiveCount = 0;
      }
    }
    return false;
  }

  private static boolean checkRow(
      Player player, int currentRow, Player[][] grid, int minConsecutiveToWin, int gridSize) {
    int consecutiveCount = 0; // Count consecutive symbols of the current player
    for (int col = 0; col < gridSize; col++) {
      if (grid[currentRow][col] == player) {
        consecutiveCount++;
        if (consecutiveCount >= minConsecutiveToWin) {
          return true;
        }
      } else {
        consecutiveCount = 0; // Reset count if symbol mismatch
      }
    }
    return false;
  }
}
