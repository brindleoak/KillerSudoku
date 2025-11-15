import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Killer {
  private record Game(boolean solved, int[] board, int[][] rules) {}

  static boolean isRelated(int i, int j) {
    return (i / 9 == j / 9) // sameRow
        || ((i - j) % 9 == 0) // sameCol
        || ((i / 27 == j / 27) && (i % 9 / 3 == j % 9 / 3)); // sameBlock
  }

  private static int[] getRelatedCells(int cell) {
    return IntStream.rangeClosed(0, 80).filter(el -> isRelated(cell, el)).toArray();
  }

  private static final int[][] relatedCells =
      IntStream.rangeClosed(0, 80).mapToObj(Killer::getRelatedCells).toArray(int[][]::new);

  private static boolean validAttempt(Game game, int number, int cell) {
    int[] cellRule = game.rules[cell];

    for (int j : relatedCells[cell]) if (game.board[j] == number) return false;

    if (cellRule == null) return true;

    int sum = 0;
    for (int i = 1; i < cellRule.length; i++) {
      sum += game.board[cellRule[i]];
    }

    return sum + number == cellRule[0];
  }

  private static Game recursiveCheck(Game game) {
    int i;
    for (i = 0; i < 81; i++) if (game.board[i] == 0) break;
    if (i == 81) {
      return new Game(true, game.board, game.rules);
    }

    for (int m = 1; m < 10; m++) {
      if (validAttempt(game, m, i)) {
        int[] newBoard = Arrays.copyOf(game.board, 81);
        newBoard[i] = m;
        Game result = recursiveCheck(new Game(false, newBoard, game.rules));
        if (result.solved) return result;
      }
    }

    return game;
  }

  public static int[] solve(List<String> ruleList) {
    int[][] rules = buildRules(ruleList);
    Game res = recursiveCheck(new Game(false, new int[81], rules));
    return res.board;
  }

  private static int[][] buildRules(List<String> ruleList) {
    int[][] rule = new int[81][];

    ruleList.stream()
        .map(line -> Stream.of(line.split(",")).mapToInt(Integer::parseInt).toArray())
        .forEach(p -> rule[p[p.length - 1]] = p);

    return rule;
  }
}
