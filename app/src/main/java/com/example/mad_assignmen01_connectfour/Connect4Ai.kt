package com.example.mad_assignmen01_connectfour

import kotlin.random.Random

class Connect4AI {
    fun getMove(board: List<MutableList<Int>>): Int {
        val availableColumns = board[0].indices.filter { board[0][it] == 0 }
        return if (availableColumns.isNotEmpty()) {
            availableColumns.random()
        } else {
            -1
        }
    }
}
