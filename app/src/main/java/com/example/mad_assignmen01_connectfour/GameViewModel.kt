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
    private val ai = Connect4AI()
    var isSinglePlayer by mutableStateOf(false)

    fun initialise(boardWidth: Int, boardHeight: Int, is1P: Boolean) {
        width = boardWidth
        height = boardHeight
        board = Board(rows = height, columns = width)
        isSinglePlayer = is1P
    }

    fun setIsSinglePlayerTrue() {
        isSinglePlayer = true
    }

    fun changeIsSinglePlayer(is1P: Boolean) {
        isSinglePlayer = is1P
    }

    var board by mutableStateOf(Board(rows = height, columns = width)) // individual board state
    private val moveStack = Stack<Board>() // stack of previous board states
    var currentPlayer by mutableIntStateOf(1)
    var gameMessage by mutableStateOf("")
    var isGameOver by mutableStateOf(false)

    /** Set board to initial blank state, clear move stack */
    fun resetBoard() {
        board = Board(rows = height, columns = width)
        currentPlayer = 1
        gameMessage = ""
        isGameOver = false
        moveStack.clear()
    }

    /** update game message if game is over */
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

    /** push current board state */
    fun saveStateToUndoStack() {
        moveStack.push(board.copy())
    }

    fun makeAIMove() {
        val aiMoveColumn = ai.getMove(board.boardState)
        if (aiMoveColumn != -1) {
            board.placePiece(col = aiMoveColumn, player = 2) // assume AI makes valid moves
            currentPlayer = 1
            checkForAndHandleWin()
        }
    }

    /** make move in current row (if valid) and add to stack */
    fun makePlayerMove(col: Int): Boolean {
        moveStack.push(board.copy()) // add to undo stack
        val moveWasValid = board.placePiece(col = col, player = currentPlayer) // try to make move
        if (!moveWasValid) {
            moveStack.push(board.copy()) // revert save if move invalid
        } else {
            currentPlayer = if (currentPlayer == 1) 2 else 1
            checkForAndHandleWin()
        }
        return moveWasValid
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
}
