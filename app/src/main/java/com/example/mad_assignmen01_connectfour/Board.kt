package com.example.mad_assignmen01_connectfour



/**
 * boardState is a list of mutable lists, indexed as [y,x]
 */
data class Board(
    val rows: Int,
    val columns: Int,
    var boardState: List<MutableList<Int>> = List(rows) { MutableList(columns) { 0 } }
) {
    fun reset() {
        boardState = List(rows) { MutableList(columns) { 0 } }
    }

    /** place circle in column, return false if move is invalid */
    fun placePiece(col: Int, player: Int): Boolean {
        for (r in rows - 1 downTo 0) {
            if (boardState[r][col] == 0) {
                boardState[r][col] = player
                return true
            }
        }
        return false
    }

    fun checkWin(): Int {
        for (row in boardState.indices) {
            for (col in boardState[row].indices) {
                if (boardState[row][col] != 0) {
                    val player = boardState[row][col]

                    if (col + 3 < boardState[row].size &&
                        player == boardState[row][col + 1] &&
                        player == boardState[row][col + 2] &&
                        player == boardState[row][col + 3]) {
                        return player
                    }

                    if (row + 3 < boardState.size &&
                        player == boardState[row + 1][col] &&
                        player == boardState[row + 2][col] &&
                        player == boardState[row + 3][col]) {
                        return player
                    }

                    if (row + 3 < boardState.size && col + 3 < boardState[row].size &&
                        player == boardState[row + 1][col + 1] &&
                        player == boardState[row + 2][col + 2] &&
                        player == boardState[row + 3][col + 3]) {
                        return player
                    }

                    if (row - 3 >= 0 && col + 3 < boardState[row].size &&
                        player == boardState[row - 1][col + 1] &&
                        player == boardState[row - 2][col + 2] &&
                        player == boardState[row - 3][col + 3]) {
                        return player
                    }
                }
            }
        }
        return 0
    }

    fun isDraw(): Boolean {
        return boardState.all { row -> row.all { it != 0 } }
    }

    fun copy(): Board {
        val newBoardState = boardState.map { it.toMutableList() }
        return Board(rows, columns, newBoardState)
    }
}
