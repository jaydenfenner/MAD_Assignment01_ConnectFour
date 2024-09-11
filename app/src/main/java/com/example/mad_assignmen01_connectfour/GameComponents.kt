package com.example.mad_assignmen01_connectfour

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.util.Stack
import com.example.mad_assignmen01_connectfour.ui.theme.MAD_Assignmen01_ConnectFourTheme

@Composable
fun GameStartMenu(navController: NavHostController, isSinglePlayer: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = if (isSinglePlayer) "Single Player Game" else "Two Player Game")
        Button(
            onClick = {
                if (isSinglePlayer) {
                    navController.navigate(Routes.GAME_1P)
                } else {
                    navController.navigate(Routes.GAME_2P)
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start Game")
        }
    }
}


@Composable
fun Connect4Board(rows: Int = 6, columns: Int = 7, isSinglePlayer: Boolean) {
    var board by remember { mutableStateOf(List(rows) { MutableList(columns) { 0 } }) }
    var currentPlayer by remember { mutableStateOf(1) }
    var gameMessage by remember { mutableStateOf("") }
    var gameOver by remember { mutableStateOf(false) }
    val ai = Connect4AI()
    val moveStack = remember { Stack<List<MutableList<Int>>>() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = Color.LightGray)
            .padding(16.dp)
    ) {
        for (row in 0 until rows) {
            Row {
                for (col in 0 until columns) {
                    Connect4Cell(
                        state = board[row][col],
                        onClick = {
                            if (!gameOver) {
                                handleCellClick(row, col, board, currentPlayer, moveStack) { updatedBoard, nextPlayer ->
                                    board = updatedBoard
                                    val winner = checkWin(board)
                                    if (winner != 0) {
                                        gameMessage = "Player $winner Wins!"
                                        gameOver = true
                                    } else if (isDraw(board)) {
                                        gameMessage = "It's a Draw!"
                                        gameOver = true
                                    } else {
                                        currentPlayer = nextPlayer
                                    }

                                    if (isSinglePlayer && currentPlayer == 2 && !gameOver) {
                                        val aiMove = ai.getMove(board)
                                        if (aiMove != -1) {
                                            handleCellClick(0, aiMove, board, 2, moveStack) { updatedBoard, nextPlayer ->
                                                board = updatedBoard
                                                val aiWinner = checkWin(board)
                                                if (aiWinner != 0) {
                                                    gameMessage = "Player $aiWinner Wins!"
                                                    gameOver = true
                                                } else if (isDraw(board)) {
                                                    gameMessage = "It's a Draw!"
                                                    gameOver = true
                                                } else {
                                                    currentPlayer = nextPlayer
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
        Text(text = "Current Turn: Player $currentPlayer", modifier = Modifier.padding(top = 16.dp))

        if (gameMessage.isNotEmpty()) {
            Text(text = gameMessage, modifier = Modifier.padding(top = 16.dp), color = Color.Red)
        }

        Button(
            onClick = {
                if (moveStack.isNotEmpty()) {
                    board = moveStack.pop()
                    currentPlayer = if (currentPlayer == 1) 2 else 1
                    gameMessage = ""
                    gameOver = false
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Undo Move")
        }

        Button(
            onClick = {
                moveStack.clear()
                board = List(rows) { MutableList(columns) { 0 } }
                currentPlayer = 1
                gameMessage = ""
                gameOver = false
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Reset Game")
        }
    }
}




@Composable
fun Connect4Cell(state: Int, onClick: () -> Unit) {
    val color = when (state) {
        1 -> Color.Red
        2 -> Color.Yellow
        else -> Color.White
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .padding(4.dp)
            .border(2.dp, Color.Black)
            .background(Color.Blue)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Circle(color = color)
    }
}

@Composable
fun Circle(color: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color, shape = CircleShape)
    )
}

fun handleCellClick(
    row: Int,
    col: Int,
    board: List<MutableList<Int>>,
    currentPlayer: Int,
    moveStack: Stack<List<MutableList<Int>>>,
    updateBoard: (List<MutableList<Int>>, Int) -> Unit
) {
    // Save the current board state to the stack before updating
    moveStack.push(board.map { it.toMutableList() })

    for (r in board.size - 1 downTo 0) {
        if (board[r][col] == 0) {
            board[r][col] = currentPlayer
            updateBoard(board, if (currentPlayer == 1) 2 else 1)
            break
        }
    }
}

fun checkWin(board: List<MutableList<Int>>): Int {
    for (row in board.indices) {
        for (col in board[row].indices) {
            if (board[row][col] != 0) {
                val player = board[row][col]

                if (col + 3 < board[row].size &&
                    player == board[row][col + 1] &&
                    player == board[row][col + 2] &&
                    player == board[row][col + 3]) {
                    return player
                }

                if (row + 3 < board.size &&
                    player == board[row + 1][col] &&
                    player == board[row + 2][col] &&
                    player == board[row + 3][col]) {
                    return player
                }

                if (row + 3 < board.size && col + 3 < board[row].size &&
                    player == board[row + 1][col + 1] &&
                    player == board[row + 2][col + 2] &&
                    player == board[row + 3][col + 3]) {
                    return player
                }

                if (row - 3 >= 0 && col + 3 < board[row].size &&
                    player == board[row - 1][col + 1] &&
                    player == board[row - 2][col + 2] &&
                    player == board[row - 3][col + 3]) {
                    return player
                }
            }
        }
    }
    return 0
}

fun isDraw(board: List<MutableList<Int>>): Boolean {
    return board.all { row -> row.all { it != 0 } }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview(isSinglePlayer: Boolean = false) {
    MAD_Assignmen01_ConnectFourTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileDisplay()
            Connect4Board(isSinglePlayer = isSinglePlayer)
        }
    }
}


@Composable
fun ProfileDisplay() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(75.dp)
            ) {
                Text(text = "")
            }
            Text(text = "Player 1")
        }

        Spacer(modifier = Modifier.width(100.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(75.dp)
            ) {
                Text(text = "")
            }
            Text(text = "Player 2")
        }
    }
}
