package com.example.mad_assignmen01_connectfour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mad_assignmen01_connectfour.ui.theme.MAD_Assignmen01_ConnectFourTheme

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") { MainMenu(navController) }
        composable("connect4") { DefaultPreview() }
    }
}

@Composable
fun MainMenu(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Main Menu")
        Button(
            onClick = { navController.navigate("connect4") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start Connect 4")
        }
    }
}

@Composable
fun Connect4Board(rows: Int = 6, columns: Int = 7) {
    var board by remember { mutableStateOf(List(rows) { MutableList(columns) { 0 } }) }
    var currentPlayer by remember { mutableStateOf(1) }
    var gameOver by remember { mutableStateOf(false) }
    var winner by remember { mutableStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = Color.LightGray)
            .padding(16.dp)
    ) {
        // Draw the board
        for (row in 0 until rows) {
            Row {
                for (col in 0 until columns) {
                    Connect4Cell(
                        state = board[row][col],
                        onClick = {
                            if (!gameOver) {
                                handleCellClick(row, col, board, currentPlayer) { updatedBoard, nextPlayer ->
                                    board = updatedBoard
                                    currentPlayer = nextPlayer

                                    winner = checkWin(board)
                                    if (winner != 0) {
                                        gameOver = true
                                    } else if (isDraw(board)) {
                                        gameOver = true
                                        winner = -1
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        Text(
            text = if (gameOver) {
                when (winner) {
                    1 -> "Player 1 Wins!"
                    2 -> "Player 2 Wins!"
                    -1 -> "It's a Draw!"
                    else -> ""
                }
            } else {
                "Current Turn: Player $currentPlayer"
            },
            modifier = Modifier.padding(top = 16.dp),
            color = Color.Black
        )
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
    updateBoard: (List<MutableList<Int>>, Int) -> Unit
) {
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
    return 0 // No winner
}

fun isDraw(board: List<MutableList<Int>>): Boolean {
    return board.all { row -> row.all { it != 0 } }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MAD_Assignmen01_ConnectFourTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            profileDisplay()
            Connect4Board()
        }
    }
}

@Composable
fun profileDisplay() {
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
