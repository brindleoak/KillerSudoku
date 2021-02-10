import Foundation

var rules = Array(repeating:[], count: 81)

getParms()
print("started...")
recursiveCheck(board: Array(repeating:0, count: 81))

func getParms() {
    let lines = try! NSString(contentsOfFile: "/home/simon/Killer/C/killer/puzzle.csv",
        encoding: String.Encoding.ascii.rawValue)

    lines.enumerateLines({ (line, stop) -> () in
        let cells = line.split(separator: ",").map { Int($0)!}
        rules[cells.last!] = cells
        })
}

func checkRow(board: [Int], cell: Int, val: Int) -> Bool {
    if rules[cell].count > 0 {
        let rule = (rules[cell] as? [Int])!
        if rule[0] != val + (rule[1...].map {board[$0]}.reduce(0, +)) {
            return false
        }
    }
    return true
}

func recursiveCheck(board: [Int]) {
    let i = board.firstIndex(of: 0) ?? -1
    if i == -1 {
        print(board)
        exit(0)
    }

    var excludedNumbers: Set<Int> = []
    for j in 0...i {
	    if (i/9 == j/9) || (i-j) % 9 == 0 || (i/27 == j/27 && i%9/3 == j%9/3) {
            if board[j] != 0 {
                excludedNumbers.insert(board[j])
            }
        }
    }

    for m in 1...9 {
        if !excludedNumbers.contains(m) {
            if checkRow(board: board, cell: i, val: m) {
                var newBoard = board
                newBoard[i] = m
                recursiveCheck(board: newBoard)
            }
        }
    }
}
