package com.example.mad_assignmen01_connectfour

import android.util.Log
import kotlin.math.max
import kotlin.math.min

/**
 * Class to handle the minimax search tree of positions
 */
class AI {
    /**
     * interface to ensure different AIs contain the same methods
     */
    interface MinimaxAI {
        /** should never be called in a draw position */
        fun getMove(pos: Position): Int
    }

    /**
     * Makes winning move if available at depth 0, searches ahead 'depth' moves
     * Otherwise plays first valid move from centre outwards
     *
     * NOTE ONLY FOR PLAYER 2 FOR NOW
     */
    class EvenWeight(val lookAhead: Int): MinimaxAI {
        override fun getMove(pos: Position): Int {
            /** play winning move if available */
            val winningMove = pos.checkWinningMove()
            if (winningMove != -1) return winningMove

            /** check valid moves, if lookahead = 0 just play first valid move */
            val movesToTry = getValidMovesInOrder(pos)
            if (lookAhead == 0) return movesToTry[0] // return first valid move if no lookahead

            /** play best candidate move after looking ahead to 'lookAhead' depth */
            var bestScore = Int.MIN_VALUE
            var bestMove = movesToTry[0] // default to closest to center if all losing (should rarely happen)
            Log.d("validMoves", "trying moves: ${movesToTry.contentToString()}")
            for (x in movesToTry) {
                val score = minimax(
                    pos = pos.spawnNextPosition(x),
                    depth = lookAhead-1,
                    heuristicFunction = PositionHeuristics::alwaysDraw,
                    tempPlayed = x
                )
                Log.d("score", "score for move: $x is $score")
                // this comparison implicitly plays equal scoring moves from center outwards
                if (score > bestScore) {
                    bestScore = score
                    bestMove = x
                }
            }
            return bestMove
        }
    }
}

/**
 * return array of valid cols to try starting from the middle and working outwards
 */
fun getValidMovesInOrder(pos: Position): IntArray {
    val validMovesMask = pos.validMovesMask()
    var moves = IntArray(0)
    val center = pos.boardWidth / 2 - 1 // integer division, left of centre if even size

    if (validMovesMask[center]) moves += center // add center if valid

    for (dx in 1 .. pos.boardWidth / 2) { // loop over possible distances from center
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
    tempPlayed: Int=0
): Int {
    /** base case 1: draw game */
    if (pos.isDraw()) return 0

    /** base case 2: win if winning move is available */
    if (pos.checkWinningMove() != -1) { // a winning move was found for the current player
        return if (pos.currentPlayer == 2) Int.MAX_VALUE else Int.MIN_VALUE
    }

    /** base case 1: depth is 0 -> perform static evaluation of position */
    if (depth == 0) return heuristicFunction(pos)

    /** else recursively check moves in order */
    val orderedMoves = getValidMovesInOrder(pos)
    if (pos.currentPlayer == 2) { // p2 is maximising player
        var maxScore: Int = Int.MIN_VALUE
        for (move in orderedMoves) {
            val childPos = pos.spawnNextPosition(x = move)
            val score = minimax(pos = childPos, depth = depth-1, heuristicFunction)
            maxScore = max(score, maxScore)
        }
        return maxScore
    }
    else { // p1 is minimising player
        var minScore: Int = Int.MAX_VALUE
        for (move in orderedMoves) {
            val childPos = pos.spawnNextPosition(x = move)
            val score = minimax(pos = childPos, depth = depth-1, heuristicFunction)
            minScore = min(score, minScore)
        }
        return minScore
    }
}