package com.example.mad_assignmen01_connectfour

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mad_assignmen01_connectfour.ui.theme.MAD_Assignmen01_ConnectFourTheme
import java.util.Stack


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
    val cellSize = (screenWidth / 10).dp
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
        Circle(color = color)
    }
}

@Composable
fun Circle(color: Color) {
    val context = LocalContext.current
    val metrics = context.resources.displayMetrics
    val screenWidth = metrics.widthPixels / metrics.density
    val screenHeight = metrics.heightPixels / metrics.density
    val cellSize = ((screenWidth/10)*.66).dp
    Box(
        modifier = Modifier
            .size(cellSize)
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
    shVm: ConnectFourViewModel = ConnectFourViewModel(),
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
            ProfileDisplay(
                leftProfile = shVm.player1Profile,
                rightProfile = if (isSinglePlayer) shVm.computerProfile else shVm.player2Profile)
            Connect4Board(gridHeight, gridWidth, isSinglePlayer = isSinglePlayer)
        }
    }
}

@Composable
fun ProfileImageClickable(
    modifier: Modifier = Modifier,
    userProfile: UserProfile,
    onClick: () -> Unit
) {
    FilledIconButton(
        onClick = {onClick()},
        modifier = modifier,
        colors = IconButtonDefaults.filledIconButtonColors(Color.Yellow)
    ) {
        Image(
            painter = painterResource(id = userProfile.avatarID),
            contentDescription = "",
            modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun ProfileDisplay(leftProfile: UserProfile, rightProfile: UserProfile) {
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
            ProfileImageClickable(Modifier.size(75.dp), leftProfile) { /* TODO */ }
            Text(text = leftProfile.name)
        }

        Spacer(modifier = Modifier.width(100.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            ProfileImageClickable(Modifier.size(75.dp), rightProfile) { /* TODO */ }
            Text(text = rightProfile.name)
        }
    }
}