package com.example.mad_assignmen01_connectfour

import android.util.Log
import kotlin.math.max
import kotlin.math.min

enum class AiDifficulty(val id: Int) {
    TRIVIAL(0),
    EASY(1),
    MEDIUM(2),
    HARD(3),
    IMPOSSIBLE(4),
}

/**
 * Class to handle the minimax search tree of positions
 */
class AI {
    /** interface to ensure different AIs contain the same methods */
    interface connect4Ai {
        /** should never be called in a draw position */
        fun getMove(pos: Position): Int
    }

    class RandomAI(): connect4Ai {
        override fun getMove(pos: Position): Int {
            val possibleMoves = getValidMovesInOrder(pos)
            return possibleMoves.random()
        }
    }

    /** AI with minimax:
     * Makes winning move if available at depth 0, searches ahead 'lookAhead' moves
     *
     * Heuristic ranks all non-final positions evenly, so AI plays center columns first
     */
    class EvenWeight(val lookAhead: Int): connect4Ai {
        override fun getMove(pos: Position): Int {
            return baseGetMove(pos = pos, lookAhead = lookAhead,
                heuristicFunction = PositionHeuristics::alwaysDraw
            )
        }
    }

    /** AI with minimax:
     * Makes winning move if available at depth 0, searches ahead 'lookAhead' moves
     *
     * Heuristic ranks position based on the number of open 3/4s for each player, but
     * does not consider whether those 4s are reachable (i.e. opponent win below)
     */
    class Open3s(val lookAhead: Int): connect4Ai {
        override fun getMove(pos: Position): Int {
            return baseGetMove(pos = pos, lookAhead = lookAhead,
                heuristicFunction = PositionHeuristics::numberOfOpen3s
            )
        }
    }

    /** AI with minimax:
     * Makes winning move if available at depth 0, searches ahead 'lookAhead' moves
     *
     * Heuristic ranks position based on the number of open 3/4s for each player, and
     * considers whether those 4s are reachable (i.e. opponent win below)
     */
    class EfficientOpen3s(val lookAhead: Int): connect4Ai {
        override fun getMove(pos: Position): Int {
            return baseGetMove(pos = pos, lookAhead = lookAhead,
                heuristicFunction = PositionHeuristics::efficientOpen3s
            )
        }
    }

    /** AI with minimax:
     * Makes winning move if available at depth 0, searches ahead 'lookAhead' moves
     *
     * Heuristic ranks position based on the number of open 3/4s and horizontal 2/4s
     * for each player, and considers whether 3/4s are reachable (i.e. opponent win below)
     */
    class Open2sAnd3s(val lookAhead: Int): connect4Ai {
        override fun getMove(pos: Position): Int {
            return baseGetMove(pos = pos, lookAhead = lookAhead,
                heuristicFunction = PositionHeuristics::open2sAnd3s
            )
        }
    }

//    /** AI with minimax:
//     * Makes winning move if available at depth 0, searches ahead 'lookAhead' moves
//     *
//     * Heuristic ranks position based on the number of potential 4s each players' pieces can
//     * contribute to, but does not factor whether those potential 4s are blocked
//     */
//    class WeightedPositions(val lookAhead: Int): MinimaxAI {
//        override fun getMove(pos: Position): Int {
//            return baseGetMove(pos = pos, lookAhead = lookAhead,
//                heuristicFunction = PositionHeuristics::minimaxWeightedPositions
//            )
//        }
//    }
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

    /** search minimax to lookahead depth **/
    val minimaxLookAhead = if (lookAhead < 1) 1 else lookAhead
    val (bestScore, isEndgame, bestMove) = minimaxABPruning(
        pos = pos,
        depth = minimaxLookAhead,
        alphaIn = Int.MIN_VALUE,
        betaIn = Int.MAX_VALUE,
        heuristicFunction = heuristicFunction,
    )
    Log.d("minimax", "minimax returned --> x:$bestMove, score:$bestScore")
    return bestMove
}

// TODO ############## Now with alpha-beta pruning: ##############################################
/**
 * Perform minimax search on position to given depth
 * NOTE: it is assumed that there is no winning move in the root position (checked by the wrapper)
 * @return Triple(score, isEndgame, bestMove)
 *
 * NOTE: player1 defined as minimizing, player2 maximising
 * NOTE: winning moves are still checked at depth 0 because this is relatively cheap
 */
fun minimaxABPruning(
    pos: Position,
    depth: Int,
    alphaIn: Int, // best case so far for maximising player
    betaIn: Int, // best case so far for minimising player
    heuristicFunction: (pos: Position) -> Int
): Triple<Int, Boolean, Int> {
    /** base case 1: draw game */
    if (pos.isDraw()) return Triple(0, false, -1)

    /** base case 2: win if winning move is available */
    if (pos.checkWinningMove() != -1) { // a winning move was found for the current player
        return Triple(
            if (pos.currentPlayer == 2) Int.MAX_VALUE else Int.MIN_VALUE, // score
            true, // if end of game position has been found
            -1 // the move that led to this position (ignored since win checked at top level)
        )
    }

    /** base case 1: depth is 0 -> perform static evaluation of position */
    if (depth == 0) return Triple(heuristicFunction(pos), false, -1)

    /** else recursively check moves in order */
    var alpha = alphaIn; var beta = betaIn // get mutable alpha and beta
    val orderedMoves = getValidMovesInOrder(pos)
    var bestMove = orderedMoves[0] // if all valid moves are 1 turn loss just play in middle
    var isEndgame = false // flag for whether position returned is definitely a win or loss

    if (pos.currentPlayer == 2) { // p2 is maximising player
        /** if current player is player 2, then maximise */
        var maxScore: Int = Int.MIN_VALUE
        for (move in orderedMoves) {
            val childPos = pos.spawnNextPosition(x = move)
            var (score, childIsEndgame, _) = minimaxABPruning(
                pos = childPos, depth = depth-1,
                alphaIn = alpha, betaIn = beta,
                heuristicFunction = heuristicFunction)
            // reduce win score by num moves until win, increase loss score by moves until loss
            if(childIsEndgame) {score += if(score > 0 /*(WIN)*/) -1 else 1}
            // (favours optimum play between equivalent win/loss conditions)

            if (score > maxScore) {
                maxScore = score
                isEndgame = childIsEndgame
                bestMove = move

                // update alpha if applicable and prune if possible
                alpha = max(alpha, maxScore)
                if (beta <= alpha) break
            }
        }
        return Triple(maxScore, isEndgame, bestMove)
    }
    else { // p1 is minimising player
        /** if current player is player 2, then minimise */
        var minScore: Int = Int.MAX_VALUE
        for (move in orderedMoves) {
            val childPos = pos.spawnNextPosition(x = move)
            var (score, childIsEndgame, _) = minimaxABPruning(
                pos = childPos, depth = depth-1,
                alphaIn = alpha, betaIn = beta,
                heuristicFunction = heuristicFunction)
            // reduce win score (-ve so actually increase) by num moves until win,
            // increase (-ve so actually decrease) loss score by moves until loss
            if(childIsEndgame) {score += if(score < 0 /*(WIN)*/) 1 else -1}
            // (favours optimum play between equivalent win/loss conditions)

            if (score < minScore) {
                minScore = score
                isEndgame = childIsEndgame
                bestMove = move

                // update beta if applicable and prune if possible
                beta = min(beta, minScore)
                if (beta <= alpha) break
            }
        }
        return Triple(minScore, isEndgame, bestMove)
    }
}

/**
 * Perform minimax search on position to given depth
 * NOTE: it is assumed that there is no winning move in the root position (checked by the wrapper)
 * @return Triple(score, isEndgame, bestMove)
 *
 * NOTE: player1 defined as minimizing, player2 maximising
 * NOTE: winning moves are still checked at depth 0 because this is relatively cheap
 */
fun minimax(
    pos: Position,
    depth: Int,
    heuristicFunction: (pos: Position) -> Int
): Triple<Int, Boolean, Int> {
    /** base case 1: draw game */
    if (pos.isDraw()) return Triple(0, false, -1)

    /** base case 2: win if winning move is available */
    if (pos.checkWinningMove() != -1) { // a winning move was found for the current player
        return Triple(
            if (pos.currentPlayer == 2) Int.MAX_VALUE else Int.MIN_VALUE, // score
            true, // if end of game position has been found
            -1 // the move that led to this position (ignored since win checked at top level)
        )
    }

    /** base case 1: depth is 0 -> perform static evaluation of position */
    if (depth == 0) return Triple(heuristicFunction(pos), false, -1)

    /** else recursively check moves in order */
    val orderedMoves = getValidMovesInOrder(pos)
    var bestMove = orderedMoves[0] // if all valid moves are 1 turn loss just play in middle
    var isEndgame = false // flag for whether position returned is definitely a win or loss

    if (pos.currentPlayer == 2) { // p2 is maximising player
        /** if current player is player 2, then maximise */
        var maxScore: Int = Int.MIN_VALUE
        for (move in orderedMoves) {
            val childPos = pos.spawnNextPosition(x = move)
            var (score, childIsEndgame, _) = minimax(pos = childPos, depth = depth-1, heuristicFunction)
            // reduce win score by num moves until win, increase loss score by moves until loss
            if(childIsEndgame) {score += if(score > 0 /*(WIN)*/) -1 else 1}
            // (favours optimum play between equivalent win/loss conditions)

            if (score > maxScore) {
                maxScore = score
                isEndgame = childIsEndgame
                bestMove = move
            }
        }
        return Triple(maxScore, isEndgame, bestMove)
    }
    else { // p1 is minimising player
        /** if current player is player 2, then minimise */
        var minScore: Int = Int.MAX_VALUE
        for (move in orderedMoves) {
            val childPos = pos.spawnNextPosition(x = move)
            var (score, childIsEndgame, _) = minimax(pos = childPos, depth = depth-1, heuristicFunction)
            // reduce win score (-ve so actually increase) by num moves until win,
            // increase (-ve so actually decrease) loss score by moves until loss
            if(childIsEndgame) {score += if(score < 0 /*(WIN)*/) 1 else -1}
            // (favours optimum play between equivalent win/loss conditions)

            if (score < minScore) {
                minScore = score
                isEndgame = childIsEndgame
                bestMove = move
            }
        }
        return Triple(minScore, isEndgame, bestMove)
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
