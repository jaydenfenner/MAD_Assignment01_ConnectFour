package com.example.mad_assignmen01_connectfour

import androidx.compose.runtime.MutableState
import kotlin.random.Random

class Connect4AI {
    fun getMove(board: List<List<MutableState<Int>>>): Int {
        val availableColumns = board[0].indices.filter { board[0][it].value == 0 }
        return if (availableColumns.isNotEmpty()) {
            availableColumns.random()
        } else {
            -1
        }
    }
}
