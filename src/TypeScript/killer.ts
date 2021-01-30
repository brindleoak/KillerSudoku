let values: number[][] = [
  [7, 0, 1],
  [27, 2, 9, 10, 11],
  [7, 3, 4],
  [17, 5, 6, 7, 15],
  [16, 8, 17],
  [8, 12, 13, 14],
  [14, 16, 25, 26],
  [10, 18, 19, 28],
  [13, 20, 21],
  [19, 22, 23, 32],
  [19, 24, 33, 42],
  [16, 27, 36],
  [3, 29, 38],
  [12, 30, 31],
  [9, 34, 35],
  [8, 37, 46],
  [12, 39, 48],
  [5, 40, 49],
  [14, 41, 50],
  [6, 43, 44, 53],
  [17, 45, 54, 63, 72],
  [7, 47, 56],
  [16, 51, 52],
  [16, 55, 64, 65],
  [17, 57, 66],
  [18, 58, 59, 67],
  [7, 60, 69, 70],
  [23, 61, 62, 71],
  [7, 68, 77],
  [17, 73, 74],
  [3, 75, 76],
  [15, 78, 79, 80],
];
const parms: number[][] = values.sort(
  (a, b) => a[a.length - 1] - b[b.length - 1]
);
const digits: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9];

let a: number[] = new Array(81).fill(0);
recursiveCheck(a);

function sameRow(i: number, j: number) {
  return (i / 9) >> 0 == (j / 9) >> 0;
}
function sameCol(k: number, l: number) {
  return (k - l) % 9 == 0;
}
function sameBlock(m: number, n: number) {
  return (
    (m / 27) >> 0 == (n / 27) >> 0 && ((m % 9) / 3) >> 0 == ((n % 9) / 3) >> 0
  );
}

function checkRow(aRow: number[]) {
  var pos: number = aRow.indexOf(0);
  let selectedParms: number[][] = parms.filter(
    (parm) => parm[parm.length - 1] == pos - 1
  );
  if (selectedParms.length > 0) {
    let selectedParm: number[] = selectedParms[0];
    let subParm: number[] = selectedParm.slice(1, selectedParm.length);
    let total: number = subParm.map((a) => aRow[a]).reduce((b, c) => b + c, 0);
    if (total != selectedParm[0]) {
      return false;
    } else {
      return true;
    }
  } else {
    return true;
  }
}

function recursiveCheck(inBoard: number[]) {
  let i: number = inBoard.indexOf(0);
  if (i == -1) {
    console.log(inBoard);
    return true;
  }

  let excludedNumbers = new Set();
  let x: number = 0;
  let y: number = 1;

  while (x < i) {
    if (sameRow(i, x) || sameCol(i, x) || sameBlock(i, x)) {
      excludedNumbers.add(inBoard[x]);
    }

    x++;
  }

  while (y < 10) {
    if (!excludedNumbers.has(y)) {
      let newBoard: number[] = inBoard
        .slice(0, i)
        .concat([y])
        .concat(inBoard.slice(i, 80));
      if (checkRow(newBoard)) {
        recursiveCheck(newBoard);
      }
    }
    y++;
  }
}
