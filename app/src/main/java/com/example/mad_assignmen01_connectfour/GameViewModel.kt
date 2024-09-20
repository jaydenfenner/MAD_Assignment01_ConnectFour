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

    private val randomAI = Connect4AI()

    var isSinglePlayer = false
    private var isAIPlayer1 = false
    var p1Profile = UserProfile(pName = "", pAvatarID = 0)
    var p2Profile = UserProfile(pName = "", pAvatarID = 0)
    private var hasBeenInitialised by mutableStateOf(false)

    fun initialise(boardWidth: Int, boardHeight: Int,
                   is1P: Boolean, p1_profile: UserProfile, p2_profile: UserProfile,
                   isAiP1: Boolean = false, // TODO extra arg for AI as player 1
    ) {
        if (!hasBeenInitialised) {
            width = boardWidth
            height = boardHeight
            board = Board(rows = height, columns = width) // individual board state
            isSinglePlayer = is1P
            p1Profile = p1_profile
            p2Profile = p2_profile
            hasBeenInitialised = true

            isAIPlayer1 = isAiP1 // TODO extra arg for AI as player 1
            if (isAiP1) makeAIMove() // TODO extra arg for AI as player 1
        }
    }

    var board by mutableStateOf(Board(rows = height, columns = width)) // individual board state
    val moveStack = Stack<Board>() // stack of previous board states
    var currentPlayer by mutableIntStateOf(1)
    var gameMessage by mutableStateOf("")
    var isGameOver by mutableStateOf(false)

    fun getPosition(): Position {
        val position = Position(
            boardWidth = width, boardHeight = height,
            currentPlayer = currentPlayer)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val disc = board.boardState[height-1 -y][x].value // flip height to x>y^
                if (disc != 0) {
                    position.board[x][y] = disc
                    position.columnHeight[x] += 1
                }
            }
        }
        return position
    }

    /** Set board to initial blank state, clear move stack */
    fun resetBoard() {
        board = Board(rows = height, columns = width)
        currentPlayer = 1
        gameMessage = ""
        isGameOver = false
        moveStack.clear()
        if (isAIPlayer1) makeAIMove() // TODO extra arg for AI as player 1
    }

    fun movesMade() = moveStack.count() * if(isSinglePlayer) 2 else 1
    fun movesRemaining(): Int {
        val totalSpaces = height * width
        return totalSpaces - movesMade()
    }

    /** update game message if game is over */
    private fun checkForAndHandleWin() {
        val winner = board.checkWin()
        if (winner != 0) {
            isGameOver = true
            if(winner == 1 )
            {
                gameMessage = "Player ${p1Profile.name} Wins!"
                p1Profile.numberOfWins++
                p2Profile.numberOfLosses++
            }
            else{
                gameMessage = "Player ${p2Profile.name} Wins!"
                p2Profile.numberOfWins++
                p1Profile.numberOfLosses++
            }
        } else if (board.isDraw()) {
            gameMessage = "It's a Draw!"
            isGameOver = true
            p1Profile.numberOfDraws++
            p2Profile.numberOfDraws++
        }
    }

    fun makeAIMove() {
//        val aiMoveColumn = randomAI.getMove(board.boardState) // old random AI
        val ai = AI.EvenWeight(lookAhead = 5)
        val aiMoveColumn = ai.getMove(getPosition()) // new minimax AI for current position
        if (aiMoveColumn != -1) {
            board.placePiece(col = aiMoveColumn, player = currentPlayer) // assume AI makes valid moves
            checkForAndHandleWin()
            if (!isGameOver) {
                currentPlayer = if(currentPlayer == 2) 1 else 2
            }
        }
    }

    /** make move in current row (if valid) and add to stack */
    fun makePlayerMove(col: Int) {
        moveStack.push(board.getBoardCopy()) // add to undo stack
        val moveWasValid = board.placePiece(col = col, player = currentPlayer) // try to make move
        if (!moveWasValid) {
            moveStack.pop() // revert save if move invalid
        } else {
            checkForAndHandleWin()
            if (!isGameOver) {
                currentPlayer = if (currentPlayer == 1) 2 else 1
            }
        }
    }

    /** pop one move off of move stack and update board */
    fun undoMove() {
        if (moveStack.isNotEmpty() && !isGameOver) {
            board = moveStack.pop()
            if (!isSinglePlayer) {
                currentPlayer = if (currentPlayer == 1) 2 else 1
            }
            isGameOver = false
            gameMessage = ""
        }
    }
}
