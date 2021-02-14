print("Started...")
board <- rep(0, times=81)
rules <- list()

parms <- read.csv('puzzle.csv', header=FALSE)
for (i in 1:nrow(parms)) {
    parm <- parms[i,] + 1
    parm[1] <- parm[1] - 1
    parmArray <- parm[!is.na(parm)]
    
    rules[tail(parmArray, 1)] <- list(parmArray)
}

checkRow <- function(board, cell, val) {
    rule <- rules[[cell]]
    
    if (!is.null(rule)) {
        for (i in 2:length(rule)) {
            val <- val + board[rule[i]]
        }
       
        if (rule[1] != val) {
            return(FALSE)
        }
    }
    return(TRUE)
}

recursiveCheck <- function(board) {
    #print(board, max.levels = NULL, width = 180)
    i <- match(0, board)    
    if (is.na(i)) {
        print(board)
        stop("Finished!")
        
    }

    iz <- i - 1
    
    excludedNumbers <- list()
    for (j in 0:iz) {
        if ((iz%/%9 == j%/%9) | (iz-j) %% 9 == 0 | (iz%/%27 == j%/%27 & iz%%9%/%3 == j%%9%/%3)) {
            if (board[j + 1] != 0) {
                excludedNumbers[length(excludedNumbers) + 1] <- board[j + 1]
            }
        }
    }

    for (m in 1:9) {
        if (is.na(match(m, excludedNumbers))) {
            if (checkRow(board, i, m)) {
                newBoard <- board
                newBoard[i] = m
                recursiveCheck(newBoard)
            }
        }
    }
}

recursiveCheck(board)