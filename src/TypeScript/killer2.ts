import {readFileSync} from 'fs';

const rules: number[][] = 
     readFileSync('puzzle.csv', 'utf8').split('\n')
        .map(el => el.split(',')
        .map(Number))
        .flatMap(el => el.slice(1).map(cell => [cell, el] as [number, number[]])).sort((a, b) => a[0] - b[0]).map(el => el[1])

let sameRow = (i: number, j: number): boolean => i / 9 >> 0 == j / 9 >> 0
let sameCol = (i: number, j: number): boolean => (i - j) % 9 == 0
let sameBlock = (i: number, j: number): boolean => (i / 27 >> 0) == (j / 27 >> 0) && ((i % 9) / 3 >> 0) == (j % 9) / 3 >> 0
let isRelated = (cell: number, el: number): boolean => cell != el && (sameRow(cell, el) || sameCol(cell, el) || sameBlock(cell, el))
let getRelatedCells = (cell: number): number[] => Array.from(Array(81).keys()).filter(el => isRelated(cell, el))
const relatedCells = Array.from(Array(81).keys()).map(getRelatedCells)

type Position = {
    solved: boolean,
    board: number[]
}

let recursiveCheck = (pos: Position): Position => {
    let i = pos.board.indexOf(0);
    if (i == -1) 
        return { solved: true, board: pos.board }
    else {
        let validAttempt = (m: number): boolean => !relatedCells[i].map(j => pos.board[j]).includes(m)
            && (rules[i][rules[i].length - 1] != i
            || rules[i].slice(1).map(j => pos.board[j]).reduce((a, b) => a + b, 0) + m == rules[i][0])

        let step = (pos: Position, m: number): Position => {
            if (!pos.solved && validAttempt(m)) {
                let newPos = recursiveCheck({ solved: false, board: [...pos.board.slice(0, i), m, ...pos.board.slice(i + 1)] })
                if (newPos.solved) 
                    return newPos
            }
            return pos
        }

        return [1, 2, 3, 4, 5, 6, 7, 8, 9].reduce(step, { solved: false, board: pos.board })
    }
}

const solution = recursiveCheck({solved: false, board: Array(81).fill(0)})
console.log(solution.board.join(' '));