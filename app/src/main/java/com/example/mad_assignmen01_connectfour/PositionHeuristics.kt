package com.example.mad_assignmen01_connectfour

import kotlin.math.min

/**
 * Collection of methods for determining a score for each position
 *
 * All methods take a Position instance and return a signed integer
 */
object PositionHeuristics {
    /**
     * consider open 3s as well as horizontal open 2s
     */
    fun open2sAnd3s(pos: Position): Int {
        val width = pos.boardWidth
        val height = pos.boardHeight
        val board = pos.board
        var score = efficientOpen3s(pos) * 10
        // Horizontal check
        for (x in 0 until width - 3) {
            for (y in 0 until height) {
                score += countOpen2sInLine(board[x][y], board[x + 1][y], board[x + 2][y], board[x + 3][y])
            }
        }
        // Vertical check
        for (x in 0 until width) {
            for (y in 0 until height - 3) {
                score += countOpen2sInLine(board[x][y], board[x][y + 1], board[x][y + 2], board[x][y + 3])
            }
        }
        // Diagonal (top-left to bottom-right)
        for (x in 0 until width - 3) {
            for (y in 0 until height - 3) {
                score += countOpen2sInLine(board[x][y], board[x + 1][y + 1], board[x + 2][y + 2], board[x + 3][y + 3])
            }
        }
        // Diagonal (bottom-left to top-right)
        for (x in 0 until width - 3) {
            for (y in 3 until height) {
                score += countOpen2sInLine(board[x][y], board[x + 1][y - 1], board[x + 2][y - 2], board[x + 3][y - 3])
            }
        }
        return score
    }

    /**
     * More efficient approach to the below, looking at empty squares and checking if they are winning moves for either player
     *
     * look at bottom empty slots first to avoid scoring unplayable wins (i.e. win above opponent win)
     */
    fun efficientOpen3s(pos: Position): Int {
        // left to right, start at first empty and move up (move right after win found)
        val savedCurrentPlayer = pos.currentPlayer
        val board = pos.board
        var score = 0

        for (x in 0 until pos.boardWidth) { // all columns
            for (y in pos.columnHeight[x] until pos.boardHeight) { // from first empty and up
                val maxWin = pos.isWinningMove(x, y, currentPlayer = 2)
                val minWin = pos.isWinningMove(x, y, currentPlayer = 1)
                if (maxWin) {
                    if (!minWin) score++ // max only so increase score (otherwise both so inc = 0)
                    break // win found so stop checking above
                }
                if (minWin) { // min only so decrease score
                    score--
                    break // win found so stop checking above
                }
                // (else keep looking)
            }
        }
        return score
    }




    private inline fun getUniqueInt(x: Int, y: Int): Int {
        return "${x+1}${y+1}".toInt()
    }
    /**
     * "Smarter" weighted positions heuristic which accounts for blocked 4s
     * Only assigns importance to sets of 4 which are not blocked
     */
    fun numberOfOpen3s(pos: Position): Int {
        val width = pos.boardWidth
        val height = pos.boardHeight
        val board = pos.board
        var score = 0
        var winningSpotsMax: IntArray = intArrayOf()
        var winningSpotsMin: IntArray = intArrayOf()

        fun updateScoreAndWinningSpots(scoreIncrement: Int, winningSpot: Int) {
            when (scoreIncrement) {
                1 -> {
                    if (!winningSpotsMax.contains(winningSpot)) { // no reward for two 3s ending at the same spot
                        winningSpotsMax += winningSpot
                        score += scoreIncrement
                    }
                }
                -1 -> {
                    if (!winningSpotsMin.contains(winningSpot)) { // no reward for two 3s ending at the same spot
                        winningSpotsMin += winningSpot
                        score += scoreIncrement
                    }
                }
            }
        }

        // Horizontal check
        for (x in 0 until width - 3) {
            for (y in 0 until height) {
                val (scoreIncrement, emptySpotIdx) =
                    countOpen3sInLine(board[x][y], board[x + 1][y], board[x + 2][y], board[x + 3][y])

                val winningSpot = getUniqueInt(x + emptySpotIdx, y)
                updateScoreAndWinningSpots(scoreIncrement, winningSpot)
            }
        }

        // Vertical check
        for (x in 0 until width) {
            for (y in 0 until height - 3) {
                val (scoreIncrement, emptySpotIdx) =
                    countOpen3sInLine(board[x][y], board[x][y + 1], board[x][y + 2], board[x][y + 3])

                val winningSpot = getUniqueInt(x, y + emptySpotIdx)
                updateScoreAndWinningSpots(scoreIncrement, winningSpot)
            }
        }

        // Diagonal (top-left to bottom-right)
        for (x in 0 until width - 3) {
            for (y in 0 until height - 3) {
                val (scoreIncrement, emptySpotIdx) =
                    countOpen3sInLine(board[x][y], board[x + 1][y + 1], board[x + 2][y + 2], board[x + 3][y + 3])

                val winningSpot = getUniqueInt(x + emptySpotIdx, y + emptySpotIdx)
                updateScoreAndWinningSpots(scoreIncrement, winningSpot)
            }
        }

        // Diagonal (bottom-left to top-right)
        for (x in 0 until width - 3) {
            for (y in 3 until height) {
                val (scoreIncrement, emptySpotIdx) =
                    countOpen3sInLine(board[x][y], board[x + 1][y - 1], board[x + 2][y - 2], board[x + 3][y - 3])

                val winningSpot = getUniqueInt(x + emptySpotIdx, y - emptySpotIdx)
                updateScoreAndWinningSpots(scoreIncrement, winningSpot)
            }
        }

        return score
    }
    /** Helper function to count open 4-in-a-rows */
    private inline fun countOpen3sInLine(a: Int, b: Int, c: Int, d: Int
    ): Pair<Int, Int> {
        var maxCount = 0
        var minCount = 0
        var emptyCount = 0
        var emptySpotIdx = -1

        val pieces = arrayOf(a, b, c, d)

        for ((i, piece) in pieces.withIndex()) {
            when (piece) {
                2 -> maxCount++
                1 -> minCount++
                0 -> {emptyCount++; emptySpotIdx = i}
            }
            // Early return if both players have pieces in the line
            if (maxCount > 0 && minCount > 0) return Pair(0, -1)
            // Early return if more than one empty space is found
            if (emptyCount > 1) return Pair(0, -1)
        }

        return when {
            maxCount == 3 && emptyCount == 1 -> Pair(1, emptySpotIdx)
            minCount == 3 && emptyCount == 1 -> Pair(-1, emptySpotIdx)
            else -> Pair(0, -1)
        }
    }

    /** Helper function to count open 4-in-a-rows */
    private inline fun countOpen2sInLine(a: Int, b: Int, c: Int, d: Int
    ): Int {
        var maxCount = 0
        var minCount = 0
        var emptyCount = 0

        val pieces = arrayOf(a, b, c, d)

        for (piece in pieces) {
            when (piece) {
                2 -> maxCount++
                1 -> minCount++
                0 -> emptyCount++
            }
            // Early return if both players have pieces in the line
            if (maxCount > 0 && minCount > 0) return 0
            // Early return if more than two empty spaces found
            if (emptyCount > 2) return 0
            // Early return if more than two of either player found
            if (maxCount > 2 || minCount > 2) return 0
        }

        return when {
            maxCount == 2 && emptyCount == 2 -> 1
            minCount == 2 && emptyCount == 2 -> -1
            else -> 0
        }
    }


    /**
     * Simplest, just assign equal (draw) weight to all positions
     */
    fun alwaysDraw(pos: Position) = 0

    /**
     * Extremely cheap heuristic for max depth:
     * Use pre-computed tables of weights for each slot, pass the heuristic value as an argument
     * NOTE: this function is used to initialise the heuristic, otherwise should use minimax version
     */
//    fun naiveWeightedPositions(pos: Position): Int {
//        if (pos.heuristicValue != 0) throw IllegalArgumentException("heuristic already initialised, use minimaxWeightedPositions instead")
//        val selectedBoard = getHeuristicBoard(pos.boardWidth)
//        var eval: Int = 0 // start at zero
//        for ((x, col) in pos.board.withIndex()) {
//            for ((y, cell) in col.withIndex()) {
//                if (cell != 0) {
//                    eval += if(cell == 2) selectedBoard[y][x] else -selectedBoard[y][x]
//                }
//            }
//        }
//        return eval
//    }

    /**
     * Incremental version of naiveWeightedPositions heuristic.
     * Use inside minimax to return heuristic
     */
//    fun minimaxWeightedPositions(pos: Position) = pos.heuristicValue

    /** return the appropriate heuristic board for the position */
    fun getHeuristicBoard(width: Int): Array<IntArray> {
        return when (width) {
            6 -> heuristicBoard6x5
            7 -> heuristicBoard7x6
            8 -> heuristicBoard8x7
            else -> throw IllegalArgumentException("Unsupported board width: $width")
        }
    }
    private val heuristicBoard6x5: Array<IntArray> = arrayOf(
        intArrayOf(3, 4, 5, 5, 4, 3),
        intArrayOf(4, 6, 8, 8, 6, 4),
        intArrayOf(3, 6, 9, 9, 6, 3),
        intArrayOf(4, 6, 8, 8, 6, 4),
        intArrayOf(3, 4, 5, 5, 4, 3)
    )
    private val heuristicBoard7x6: Array<IntArray> = arrayOf(
        intArrayOf(3, 4, 5, 7, 5, 4, 3),
        intArrayOf(4, 6, 8, 10, 8, 6, 4),
        intArrayOf(5, 8, 11, 13, 11, 8, 5),
        intArrayOf(5, 8, 11, 13, 11, 8, 5),
        intArrayOf(4, 6, 8, 10, 8, 6, 4),
        intArrayOf(3, 4, 5, 7, 5, 4, 3)
    )
    private val heuristicBoard8x7: Array<IntArray> = arrayOf(
        intArrayOf(3, 4, 5, 7, 7, 5, 4, 3),
        intArrayOf(4, 6, 8, 10, 10, 8, 6, 4),
        intArrayOf(5, 8, 11, 13, 13, 11, 8, 5),
        intArrayOf(7, 10, 13, 16, 16, 13, 10, 7),
        intArrayOf(5, 8, 11, 13, 13, 11, 8, 5),
        intArrayOf(4, 6, 8, 10, 10, 8, 6, 4),
        intArrayOf(3, 4, 5, 7, 7, 5, 4, 3)
    )
}

//fun testGetScore(pos: Position): Int {
//    return PositionHeuristics.randomPlay(pos)
//}