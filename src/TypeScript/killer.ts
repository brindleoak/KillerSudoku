const fs = require('fs')
const startTime: Date = new Date()
var values: number[][] = [[]]

try {
  const lines = fs.readFileSync('puzzle.csv', 'utf8').split('\n')
  for (var line of lines) {
    values.push(line.split(',').map(Number)); 
  }
  values.splice(0, 1)

} catch (err) {
  console.error(err)
}

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
    console.log(((new Date()).getTime() - startTime.getTime()) / 1000.0)
    return process.exit(0);
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
