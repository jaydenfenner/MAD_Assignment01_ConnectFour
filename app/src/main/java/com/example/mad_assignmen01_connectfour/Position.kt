package com.example.mad_assignmen01_connectfour

import kotlin.math.max
import kotlin.math.min

/**
 * Each instance stores a game position and provides methods to
 * assist in evaluating it
 *
 * accessed as Position.board[x][y] or Position.board[col][row]]
 *
 * current player is 1 for red (usually human) and 2 for yellow (usually AI)
 */
class Position(val boardWidth: Int, val boardHeight: Int,
               var currentPlayer: Int
) {
    var board = Array(boardWidth) { IntArray(boardHeight) { 0 } } // accessed as board
    var columnHeight = IntArray(boardWidth) {0}

    /**
     * make move in x and return new position instance after move
     * (toggle current player)
     */
    fun spawnNextPosition(x: Int): Position {
        // clone the current position
        val nextPos = Position(boardWidth, boardHeight, currentPlayer)
        nextPos.board = Array(board.size) { board[it].clone() } // pass by value copy of board
        nextPos.columnHeight = columnHeight.clone()

        nextPos.makeMove(x) // make move in next position
        return nextPos
    }

    /***
     * place piece in board and toggle current player
     * (Must be called with a valid move)
     */
    fun makeMove(x: Int) {
        board[x][columnHeight[x]] = currentPlayer
        columnHeight[x] += 1
        currentPlayer = if(currentPlayer == 2) 1 else 2
    }

    /** return true if the game is a draw (i.e. board is full) */
    fun isDraw() = true !in validMovesMask()

    /**
     * return a boolean mask of valid moves with False for unplayable moves (full column)
     */
    fun validMovesMask(): Array<Boolean> = Array(boardWidth) {canPlay(it)}

    /**
     * return x for winning move if available, else -1
     */
    fun checkWinningMove(): Int {
        for (x in 0 until boardWidth) {
            if (canPlay(x) && isWinningMove(x)) return x
        }
        return -1
    }

    /**
     * check if playing in the specified column will win for current player
     * (cheaper than creating a new position, then checking if it is a win)
     */
    private fun isWinningMove(x: Int): Boolean {
        val moveHeight = columnHeight[x]

        // check down (only if high enough)
        if ((moveHeight >= 3)
            && (board[x][moveHeight-1] == currentPlayer)
            && (board[x][moveHeight-2] == currentPlayer)
            && (board[x][moveHeight-3] == currentPlayer)) return true

        // check left within valid bounds (left-to-right from x-3 to x-1), use dx for diagonals
        // x + dx < 0 is too far to left (note dx negative)
        var horizontal = 0; var upDiag = 0; var downDiag = 0
        for (dx in max(-x, -3) .. -1) {
            if (board[x+dx][moveHeight] == currentPlayer) horizontal += 1 else horizontal = 0
            if (moveHeight + dx >= 0) {
                if(board[x+dx][moveHeight + dx] == currentPlayer) upDiag += 1 else upDiag = 0 }
            if (moveHeight - dx < boardHeight) {
                if (board[x+dx][moveHeight - dx] == currentPlayer) downDiag += 1 else downDiag = 0 }
            // (don't need to check win since there can't be enough yet)
        }

        // increment all for x and check (now there enough positions checked)
        horizontal += 1; upDiag += 1; downDiag += 1
        if (horizontal == 4 || upDiag == 4 || downDiag == 4) return true

        // check right within valid bounds (left-to-right from x+1 to x+3), use dx for diagonals
        // x + dx >= boardWidth - 1 is too far to right
        for (dx in 1 .. min(3, boardWidth-1 -x)) {
            if (board[x+dx][moveHeight] == currentPlayer) horizontal += 1 else horizontal = 0
            if (moveHeight + dx < boardHeight) {
                if (board[x + dx][moveHeight + dx] == currentPlayer) upDiag += 1 else upDiag = 0 }
            if (moveHeight - dx >= 0) {
                if (board[x + dx][moveHeight - dx] == currentPlayer) downDiag += 1 else downDiag = 0 }

            // check for sets of 4 after updating for each location
            if (horizontal == 4 || upDiag == 4 || downDiag == 4) return true
        }
        return false
    }

    /** True if the specified column is not full */
    fun canPlay(x: Int): Boolean = (columnHeight[x] < boardHeight)

    /** return string representing board for debugging */
    fun boardString(): String {
        var output = "currentPlayer: $currentPlayer\n"
        output += "  "
        for (x in 0 until boardWidth) {
            output += if (canPlay(x)) "Y  " else "n  "
        }
        output += "\n"

        for (y in boardHeight-1 downTo 0) {
            output += "  "
            for (x in 0 until boardWidth) {
                output += "${board[x][y]}  "
            }
            if (y>0) output += "\n"
        }

        output += "\nW:\n  "
        for (x in 0 until boardWidth) {
            output += if (canPlay(x) && isWinningMove(x)) "Y  " else "n  "
        }
        output += "\n  "
        for (x in 0 until boardWidth) {
            output += "${columnHeight[x]}  "
        }
        return output
    }
}




















//fun isWinningMove(x: Int): Boolean {
//    val moveHeight = columnHeight[x]
//    Log.d("ISWINNING", "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ISwINNING x:$x, moveHeight:$moveHeight %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
//
//    // check down (only if high enough)
//    if ((moveHeight >= 3)
//        && (board[x][moveHeight-1] == currentPlayer)
//        && (board[x][moveHeight-2] == currentPlayer)
//        && (board[x][moveHeight-3] == currentPlayer)) return true
//
//    // count right and left to check horizontal
//    // check from dx = -3 to +3 or less if x near edge
//    var horizontal = 0; var upDiag = 0; var downDiag = 0
//    for (dx in max(-x, -3) until 0) {
//        Log.d("left", "START pl:$currentPlayer x: $x dx: $dx, bounds: [${x+max(-x, -3)},${x-1}]")
//        if (board[x+dx][moveHeight] == currentPlayer) {
//            horizontal += 1
//            Log.d("LH", "horizontal: $horizontal")
//        } else horizontal = 0
//        if (moveHeight + dx >= 0) {
//            if(board[x+dx][moveHeight + dx] == currentPlayer) {
//                upDiag += 1
//                Log.d("LUD", "upDiag: $upDiag")
//            } else upDiag = 0
//        }
//        if (moveHeight - dx < boardHeight) {
//            if (board[x+dx][moveHeight - dx] == currentPlayer) {
//                downDiag += 1
//                Log.d("LdD", "downDiag: $downDiag")
//            } else downDiag = 0
//        }
//        Log.d("left", "END dx:$dx, h=$horizontal, ud=$upDiag, dd=$downDiag")
//    }
//    horizontal += 1; upDiag += 1; downDiag += 1 // increment all for x
//    if (horizontal == 4 || upDiag == 4 || downDiag == 4) return true
//    for (dx in 1 until min(3, boardWidth-1 -x)+1) {
//        Log.d("right", "START pl: $currentPlayer x: $x dx: $dx, xbounds: [${x+1},${x+min(3, boardWidth-1 -x)}]")
//        if (board[x+dx][moveHeight] == currentPlayer) {
//            horizontal += 1
//            Log.d("RH", "horizontal: $horizontal")
//        } else horizontal = 0
//        if (moveHeight + dx < boardHeight) {
//            if (board[x + dx][moveHeight + dx] == currentPlayer) {
//                upDiag += 1
//                Log.d("RUD", "upDiag: $upDiag")
//            } else upDiag = 0
//        }
//        if (moveHeight - dx >= 0) {
//            if (board[x + dx][moveHeight - dx] == currentPlayer) {
//                downDiag += 1
//                Log.d("RdD", "downDiag: $downDiag")
//            } else downDiag = 0
//        }
//        Log.d("right", "END dx:$dx, h=$horizontal, ud=$upDiag, dd=$downDiag")
//        if (horizontal == 4 || upDiag == 4 || downDiag == 4) return true
//    }
//    return false
//}