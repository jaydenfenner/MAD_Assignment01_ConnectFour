package com.example.mad_assignmen01_connectfour

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Stack

/**
 * Game View Model Class
 * NOTE: board dims must be set with setBoardSize(boardWidth: Int, boardHeight: Int)
 */
class GameViewModel() : ViewModel() {
    var width = 1
        private set
    var height = 1
        private set
    fun setBoardSize(boardWidth: Int, boardHeight: Int) {
        width = boardWidth
        height = boardHeight
        board = Board(rows = height, columns = width)
    }

    var board by mutableStateOf(Board(width, height)) // individual board state
    val moveStack = Stack<Board>() // stack of previous board states

    var currentPlayer by mutableIntStateOf(1)
    var gameMessage by mutableStateOf("")
    var isGameOver by mutableStateOf(false)

    /** Set board to initial blank state, clear move stack */
    fun resetBoard() {
        board = Board(width, height)
        currentPlayer = 1
        gameMessage = ""
        isGameOver = false
        moveStack.clear()
    }

    /** pop one move off of move stack and update board */
    fun undoMove() {
        if (moveStack.isNotEmpty()) {
            board = moveStack.pop()
            currentPlayer = if (currentPlayer == 1) 2 else 1
            isGameOver = false
            gameMessage = ""
        }
    }

    /** push current board state */
    fun saveStateToUndoStack() {
        moveStack.push(board.copy())
    }

    /** update game message */
    fun checkForAndHandleWin() {
        val winner = board.checkWin()
        if (winner != 0) {
            gameMessage = "Player $winner Wins!"
            isGameOver = true
        } else if (board.isDraw()) {
            gameMessage = "It's a Draw!"
            isGameOver = true
        }
    }
}
