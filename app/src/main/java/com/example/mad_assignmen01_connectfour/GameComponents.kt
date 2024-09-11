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
import android.content.Context
import android.util.DisplayMetrics
import androidx.compose.material3.TextField
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.util.Stack
import com.example.mad_assignmen01_connectfour.ui.theme.MAD_Assignmen01_ConnectFourTheme


@Composable
fun MainMenu(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Main Menu")
        Button(
            onClick = { navController.navigate("start/2player") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start 2 Player Game")
        }
        Button(
            onClick = { navController.navigate("start/1player") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start 1 Player Game")
        }
    }
}

@Composable
fun GameStartMenu(navController: NavHostController, isSinglePlayer: Boolean) {
    var player1Name by remember { mutableStateOf("Player 1") }
    var player2Name by remember { mutableStateOf("Player 2") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = if (isSinglePlayer) "Single Player Game" else "Two Player Game")

        TextField(
            value = player1Name,
            onValueChange = { player1Name = it },
            label = { Text("Enter Player 1 Name") }
        )

        if (!isSinglePlayer) {
            TextField(
                value = player2Name,
                onValueChange = { player2Name = it },
                label = { Text("Enter Player 2 Name") }
            )
        }
        Button(
            onClick = {
                if (isSinglePlayer) {
                    navController.navigate("connect4/1player/7/6/${player1Name}")
                } else {
                    navController.navigate("connect4/2player/7/6/${player1Name}/${player2Name}")
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start Standard Game (7x6)")
        }

        Button(
            onClick = {
                if (isSinglePlayer) {
                    navController.navigate("connect4/1player/6/5/${player1Name}")
                } else {
                    navController.navigate("connect4/2player/6/5/${player1Name}/${player2Name}")
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start Small Game (6x5)")
        }

        Button(
            onClick = {
                if (isSinglePlayer) {
                    navController.navigate("connect4/1player/8/7/${player1Name}")
                } else {
                    navController.navigate("connect4/2player/8/7/${player1Name}/${player2Name}")
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start Large Game (8x7)")
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
    val context = LocalContext.current
    val metrics = context.resources.displayMetrics
    val screenWidth = metrics.widthPixels / metrics.density
    val screenHeight = metrics.heightPixels / metrics.density

    val cellSize = if (screenWidth > screenHeight) {
        (screenHeight / 13).dp
    } else {
        (screenWidth / 10).dp
    }

    val color = when (state) {
        1 -> Color.Red
        2 -> Color.Yellow
        else -> Color.White
    }

    Box(
        modifier = Modifier
            .size(cellSize)
            .padding(4.dp)
            .border(2.dp, Color.Black)
            .background(Color.Blue)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Circle(color = color, cellSize = cellSize)
    }
}

@Composable
fun Circle(color: Color, cellSize: Dp) {
    Box(
        modifier = Modifier
            .size(cellSize * 0.66f)
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
fun DefaultPreview(
    isSinglePlayer: Boolean = false,
    gridWidth: Int = 7,
    gridHeight: Int = 6,
    player1Name: String = "Player 1",
    player2Name: String = "Player 2"
) {
    MAD_Assignmen01_ConnectFourTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            profileDisplay(player1Name, if (isSinglePlayer) "AI" else player2Name)
            Connect4Board(gridHeight, gridWidth, isSinglePlayer = isSinglePlayer)
        }
    }
}




@Composable
fun profileDisplay(player1Name: String, player2Name: String) {
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
            Text(text = player1Name)
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
            Text(text = player2Name)
        }
    }
}