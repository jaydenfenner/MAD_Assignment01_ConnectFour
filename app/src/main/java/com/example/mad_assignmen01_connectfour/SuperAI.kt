package com.example.mad_assignmen01_connectfour

import android.util.Log
import kotlin.math.max
import kotlin.math.min

/**
 * Class to handle the minimax search tree of positions
 */
class AI {
    /** interface to ensure different AIs contain the same methods */
    interface MinimaxAI {
        /** should never be called in a draw position */
        fun getMove(pos: Position): Int
    }

    /** AI with minimax:
     * Makes winning move if available at depth 0, searches ahead 'lookAhead' moves
     *
     * Heuristic ranks all non-final positions evenly, so AI plays center columns first
     */
    class EvenWeight(val lookAhead: Int): MinimaxAI {
        override fun getMove(pos: Position): Int {
            return baseGetMove(pos = pos, lookAhead = lookAhead,
                heuristicFunction = PositionHeuristics::alwaysDraw
            )
        }
    }
}

/**
 * Makes winning move if available at depth 0, searches ahead 'lookAhead' moves
 * @param heuristicFunction determines how to rank candidate positions at max depth
 */
fun baseGetMove(pos: Position, lookAhead: Int,
                heuristicFunction: (pos: Position) -> Int
): Int {
    /** play winning move if available */
    val winningMove = pos.checkWinningMove()
    if (winningMove != -1) return winningMove

    /** check valid moves, if lookahead = 0 just play first valid move */
    val movesToTry = getValidMovesInOrder(pos)
    if (lookAhead == 0) return movesToTry[0] // return first valid move if no lookahead

    /** play best candidate move after looking ahead to 'lookAhead' depth */
    var bestMove = movesToTry[0] // default to closest to center if all losing (should rarely happen)

    // set initial best score and isBetter(score, bestScore) for min/max player (p1/p2)
    var bestScore = if(pos.currentPlayer == 2) Int.MIN_VALUE else Int.MAX_VALUE
    fun isBetter(score: Int, bestScore: Int): Boolean {
        return if (pos.currentPlayer == 2) (score > bestScore)
        else (score < bestScore)
    }
    Log.d("validMoves", "trying moves: ${movesToTry.contentToString()}")
    for (x in movesToTry) {
        val (score, _) = minimax(
            pos = pos.spawnNextPosition(x),
            depth = lookAhead-1,
            heuristicFunction = heuristicFunction,
        )
        Log.d("score", "score for move: $x is $score")
        // this comparison implicitly plays equal scoring moves from center outwards
        if (isBetter(score, bestScore)) {
            bestScore = score
            bestMove = x
        }
    }
    return bestMove
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
    heuristicFunction: (pos: Position) -> Int
): Pair<Int, Boolean> {
    /** base case 1: draw game */
    if (pos.isDraw()) return Pair(0, false)

    /** base case 2: win if winning move is available */
    if (pos.checkWinningMove() != -1) { // a winning move was found for the current player
        return Pair(if (pos.currentPlayer == 2) Int.MAX_VALUE else Int.MIN_VALUE, true)
    }

    /** base case 1: depth is 0 -> perform static evaluation of position */
    if (depth == 0) return Pair(heuristicFunction(pos), false)

    /** else recursively check moves in order */
    val orderedMoves = getValidMovesInOrder(pos)
    var isEndgame = false // flag for whether position returned is definitely a win or loss

    if (pos.currentPlayer == 2) { // p2 is maximising player
        /** if current player is player 2, then maximise */
        var maxScore: Int = Int.MIN_VALUE
        for (move in orderedMoves) {
            val childPos = pos.spawnNextPosition(x = move)
            var (score, childIsEndgame) = minimax(pos = childPos, depth = depth-1, heuristicFunction)
            // reduce win score by num moves until win, increase loss score by moves until loss
            if(childIsEndgame) {score += if(score > 0) -1 else 1}

            if (score > maxScore) {
                maxScore = score
                isEndgame = childIsEndgame
            }
        }
        return Pair(maxScore, isEndgame)
    }
    else { // p1 is minimising player
        /** if current player is player 2, then minimise */
        var minScore: Int = Int.MAX_VALUE
        for (move in orderedMoves) {
            val childPos = pos.spawnNextPosition(x = move)
            val (score, childIsEndgame) = minimax(pos = childPos, depth = depth-1, heuristicFunction)
            if (score < minScore) {
                minScore = score
                isEndgame = childIsEndgame
            }
        }
        return Pair(minScore, isEndgame)
    }
}

/**
 * return array of valid cols to try starting from the middle and working outwards
 */
fun getValidMovesInOrder(pos: Position): IntArray {
    val validMovesMask = pos.validMovesMask()
    var moves = IntArray(0)
    val center = (pos.boardWidth - 1) / 2 // integer division, left of centre if even size

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
