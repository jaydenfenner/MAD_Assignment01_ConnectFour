package com.example.mad_assignmen01_connectfour

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import android.content.res.Configuration
import androidx.compose.material3.TextField
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
fun GameControls(
    moveStack: Stack<Board>,
    onUndo: (Board, Int, String) -> Unit,
    onReset: (Int, Int) -> Unit,
    rows: Int,
    columns: Int,
    currentPlayer: MutableState<Int>,
    gameMessage: MutableState<String>,
    gameOver: MutableState<Boolean>
) {
    Button(
        onClick = {
            if (moveStack.isNotEmpty()) {
                val previousBoard = moveStack.pop()
                val newCurrentPlayer = if (currentPlayer.value == 1) 2 else 1
                onUndo(previousBoard, newCurrentPlayer, "")
            }
        },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(text = "Undo Move")
    }

    Button(
        onClick = {
            moveStack.clear()
            onReset(rows, columns)
        },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(text = "Reset Game")
    }
}




@Composable
fun Connect4Board(
    gameVm: GameViewModel,
    rows: Int,
    columns: Int,
    isSinglePlayer: Boolean,
) {
    val ai = Connect4AI()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

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
                        state = gameVm.board.boardState[row][col],
                        onClick = {
                            if (!gameVm.isGameOver) {
                                handleCellClick(
                                    row = row, col = col,
                                    board = gameVm.board,
                                    currentPlayer = gameVm.currentPlayer,
                                    moveStack = gameVm.moveStack,
                                ) { updatedBoard, nextPlayer ->
                                    gameVm.saveStateToUndoStack()
                                    gameVm.board = updatedBoard

                                    gameVm.checkForAndHandleWin()
                                    if (!gameVm.isGameOver) {
                                        gameVm.currentPlayer = nextPlayer
                                    }

                                    if (isSinglePlayer && gameVm.currentPlayer == 2 && !gameVm.isGameOver) {
                                        val aiMove = ai.getMove(gameVm.board.boardState)
                                        if (aiMove != -1) {
                                            handleCellClick(
                                                row = 0, col = aiMove,
                                                board = gameVm.board,
                                                currentPlayer = 2,
                                                moveStack = gameVm.moveStack
                                            ) { updatedBoard, nextPlayer ->
                                                gameVm.board = updatedBoard

                                                gameVm.checkForAndHandleWin()
                                                if (!gameVm.isGameOver) {
                                                    gameVm.currentPlayer = nextPlayer
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
        Text(
            text = "Current Turn: Player ${gameVm.currentPlayer}",
            modifier = Modifier.padding(top = 16.dp)
        )

        if (gameVm.gameMessage.isNotEmpty()) {
            Text(
                text = gameVm.gameMessage,
                modifier = Modifier.padding(top = 16.dp),
                color = Color.Red
            )
        }

//        if (!isLandscape) {
//            GameControls(
//                moveStack = moveStack,
//                onUndo = { previousBoard, newCurrentPlayer, message ->
//                    gameViewModel.undoMove()
//                },
//                onReset = { rows, columns ->
//                    gameViewModel.resetBoard(rows, columns)
//                },
//                rows = rows,
//                columns = columns,
//                currentPlayer = currentPlayer,
//                gameMessage = gameMessage,
//                gameOver = gameOver
//            )
//        }
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
    board: Board,
    currentPlayer: Int,
    moveStack: Stack<Board>,
    updateBoard: (Board, Int) -> Unit
) {
    moveStack.push(board.copy())

    if (board.placePiece(col, currentPlayer)) {
        updateBoard(board, if (currentPlayer == 1) 2 else 1)
    }
}




@Preview(showBackground = true)
@Composable
fun Preview_GameScreen() {
    val shVm = viewModel<ConnectFourViewModel>()
    val gameVm = viewModel<GameViewModel>()
    GameScreen(shVm,
        isSinglePlayer = true,
        gridWidth = 8,
        gridHeight = 8,
    )
}

@Composable
fun GameScreen(
    shVm: ConnectFourViewModel,
    isSinglePlayer: Boolean = false,
    gridWidth: Int,
    gridHeight: Int,
) {
    // initialise game view model and set board dims
    val gameVm = viewModel<GameViewModel>()
    gameVm.setBoardSize(boardWidth = gridWidth, boardHeight = gridHeight)

    val player1 = if (isSinglePlayer) shVm.singlePlayerProfileSelection
                    else shVm.twoPlayerProfileSelectionP1
    val player2 = if (isSinglePlayer) shVm.computerProfile
                    else shVm.twoPlayerProfileSelectionP2
    MAD_Assignmen01_ConnectFourTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileDisplay(
                leftProfile = player1,
                rightProfile = player2,
            )
            Connect4Board(
                gameVm = gameVm,
                rows = gridWidth,
                columns = gridHeight,
                isSinglePlayer = isSinglePlayer,
            )
        }
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
        ProfileSelectorGridItem(
            isSelected = true, // TODO need to update on game state
            userProfile = leftProfile,
            onClick = {}
        )

        Spacer(modifier = Modifier.width(100.dp))

        ProfileSelectorGridItem(
            isSelected = true, // TODO need to update on game state
            userProfile = rightProfile,
            onClick = {}
        )
    }
}