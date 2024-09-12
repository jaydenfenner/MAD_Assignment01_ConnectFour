package com.example.mad_assignmen01_connectfour

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Stack

class GameViewModel : ViewModel() {
    var board = mutableStateOf(Board(6, 7))
    val currentPlayer = mutableStateOf(1)
    val gameMessage = mutableStateOf("")
    val gameOver = mutableStateOf(false)
    val moveStack = Stack<Board>()


    fun resetBoard(rows: Int, columns: Int) {
        board.value = Board(rows, columns)
        currentPlayer.value = 1
        gameMessage.value = ""
        gameOver.value = false
        moveStack.clear()
    }

    fun undoMove() {
        if (moveStack.isNotEmpty()) {
            board.value = moveStack.pop()
            currentPlayer.value = if (currentPlayer.value == 1) 2 else 1
            gameOver.value = false
            gameMessage.value = ""
        }
    }

    fun saveMove() {
        moveStack.push(board.value.copy())
    }

    fun handleWin() {
        val winner = board.value.checkWin()
        if (winner != 0) {
            gameMessage.value = "Player $winner Wins!"
            gameOver.value = true
        } else if (board.value.isDraw()) {
            gameMessage.value = "It's a Draw!"
            gameOver.value = true
        }
    }
}
