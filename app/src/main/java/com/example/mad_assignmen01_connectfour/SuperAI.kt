package com.example.mad_assignmen01_connectfour

/**
 * Class to handle the minimax search tree of positions
 */
class SuperAI {
    /**
     * interface to ensure different AIs contain the same methods
     */
    interface MinimaxAI {
        fun getMove(pos: Position): Int
    }

    class evenWeight: MinimaxAI {
        override fun getMove(pos: Position): Int {
            return 0
        }
    }
}

/**
 * return array of valid cols to try starting from the middle and working outwards
 */
fun getValidMovesInOrder(pos: Position): IntArray {
    val validMovesMask = pos.validMovesMask()
    var moves = IntArray(0)
    val center = pos.boardWidth / 2 // integer division, left of centre if even size
    if (validMovesMask[center]) {
        moves += center
    }
    for (dx in 1 until pos.boardWidth / 2) {
        if (validMovesMask[center + dx]) {
            moves += center + dx // right of center
        }
        if (dx <= center && validMovesMask[center - dx]) {
            moves += center - dx // left of center
        }
    }
    return moves
}

/**
 * Perform minimax search on position to given depth
 * @return Pair(score, move) = evaluation of position and best move for the current player
 *
 * NOTE: player1 defined as minimizing, player2 maximising
 * NOTE: winning moves are still checked at depth 0 because this is relatively cheap
 */
fun minimax(
    pos: Position,
    depth: Int,
    heuristicFunction: (pos: Position) -> Int,
): Pair<Int, Int> {
    /** base case 1: draw game */
    if (pos.isDraw()) return Pair(0, -1)

    /** base case 2: win if winning move is available */
    val winningMove = pos.checkWinningMove()
    if (winningMove != -1) return Pair(Int.MAX_VALUE, winningMove)

    /** base case 1: depth is 0 -> perform static evaluation of position */
    if (depth == 0) {
        return Pair(heuristicFunction(pos), -1)
    }

    /** else recursively check moves in order */
    val orderedMoves = getValidMovesInOrder(pos)
    var bestMove = -1
    if (pos.currentPlayer == 2) { // p2 is maximising player
        var maxScore: Int = Int.MIN_VALUE
        for (move in orderedMoves) {
            val childPos = pos.spawnNextPosition(x = move)
            val (score, bestChildMove) = minimax(pos = pos, depth = depth-1, heuristicFunction)
            if (score > maxScore) {
                maxScore = score
                bestMove = bestChildMove
            }
        }
        return Pair(maxScore, bestMove)
    }
    else {
        var minScore: Int = Int.MAX_VALUE
        for (move in orderedMoves) {
            val childPos = pos.spawnNextPosition(x = move)
            val (score, bestChildMove) = minimax(pos = pos, depth = depth-1, heuristicFunction)
            if (score < minScore) {
                minScore = score
                bestMove = bestChildMove
            }
        }
        return Pair(minScore, bestMove)
    }
}