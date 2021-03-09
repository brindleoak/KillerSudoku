import 'dart:collection';
import 'dart:io';

var board = List.filled(81, 0);
var related = List.generate(81, (i) => List.filled(81, ''));
var parms = HashMap<int, List<int>>();

void getParms() {
  File('/home/simon/Killer/C/killer/puzzle.csv')
      .readAsLinesSync()
      .forEach((el) {
    var a = el.split(',').map(int.parse).toList();
    parms[a[a.length - 1]] = a;
  });
}

bool checkRules(List board, int i, int m) {
  var p = parms[i];
  if (p == null) {
    return true;
  }

  var sum = m;

  for (var x = 1; x < p.length; x++) {
    if (p[x] != i) {
      if (board[p[x]] == 0) {
        return true;
      }
      sum += board[p[x]];
    }
  }

  if (sum == p[0]) {
    return true;
  } else {
    return false;
  }
}

bool checkBoardValid(List board, int i, int m) {
  bool sameRow(int i, int j) {
    return i ~/ 9 == j ~/ 9;
  }

  bool sameCol(int i, int j) {
    return (i - j) % 9 == 0;
  }

  bool sameBlock(int i, int j) {
    return ((i ~/ 27 == j ~/ 27) && (i % 9 ~/ 3 == j % 9 ~/ 3));
  }

  for (var j = 0; j < i; j++) {
    if (related[i][j] == '') {
      if (sameRow(i, j) || sameCol(i, j) || sameBlock(i, j)) {
        related[i][j] = 'Y';
        related[j][i] = 'Y';
      } else {
        related[i][j] = 'N';
        related[j][i] = 'N';
      }
    }
    if (related[i][j] == 'Y') if (board[j] == m) return false;
  }

  return true;
}

void recursiveCheck(List board) {
  var i = board.indexOf(0);
  if (i == -1) {
    print(board.toString());
    exit(0);
  }

  for (var m = 1; m < 10; m++) {
    if (checkBoardValid(board, i, m)) {
      if (checkRules(board, i, m)) {
        var newBoard = List.of(board);
        newBoard[i] = m;
        recursiveCheck(newBoard);
      }
    }
  }
}

void main(List<String> arguments) {
  print('Started...');
  getParms();
  recursiveCheck(board);
}
