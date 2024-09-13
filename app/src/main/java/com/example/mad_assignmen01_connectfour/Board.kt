package com.example.mad_assignmen01_connectfour

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf


//Testing pushsdasdsa
/**
 * boardState is a list of mutable lists, indexed as [y,x]
 */
data class Board(
    val rows: Int,
    val columns: Int,
    var boardState: List<List<MutableState<Int>>> =
        List(rows) {
            List(columns) {
                mutableIntStateOf(0)
            }
        }
) {
    fun reset() {
        boardState = List(rows) {
            List(columns) {
                mutableIntStateOf(0)
            }
        }
    }

    val test = boardState[0][0].value

    /** place circle in column, return false if move is invalid */
    fun placePiece(col: Int, player: Int): Boolean {
        for (r in rows - 1 downTo 0) {
            if (boardState[r][col].value == 0) {
                boardState[r][col].value = player
                return true
            }
        }
        return false
    }

    fun checkWin(): Int {
        for (row in boardState.indices) {
            for (col in boardState[row].indices) {
                if (boardState[row][col].value != 0) {
                    val player = boardState[row][col].value

                    if (col + 3 < boardState[row].size &&
                        player == boardState[row][col + 1].value &&
                        player == boardState[row][col + 2].value &&
                        player == boardState[row][col + 3].value) {
                        return player
                    }

                    if (row + 3 < boardState.size &&
                        player == boardState[row + 1][col].value &&
                        player == boardState[row + 2][col].value &&
                        player == boardState[row + 3][col].value) {
                        return player
                    }

                    if (row + 3 < boardState.size && col + 3 < boardState[row].size &&
                        player == boardState[row + 1][col + 1].value &&
                        player == boardState[row + 2][col + 2].value &&
                        player == boardState[row + 3][col + 3].value) {
                        return player
                    }

                    if (row - 3 >= 0 && col + 3 < boardState[row].size &&
                        player == boardState[row - 1][col + 1].value &&
                        player == boardState[row - 2][col + 2].value &&
                        player == boardState[row - 3][col + 3].value) {
                        return player
                    }
                }
            }
        }
        return 0
    }

    fun isDraw(): Boolean {
        return boardState.all { row -> row.all { it.value != 0 } }
    }

    fun copy(): Board {
        val newBoardState = boardState.map { it.toMutableList() }
        return Board(rows, columns, newBoardState)
    }
}
