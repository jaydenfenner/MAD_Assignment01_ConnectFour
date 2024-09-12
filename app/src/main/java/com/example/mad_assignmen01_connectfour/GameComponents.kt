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
import androidx.navigation.NavHostController
import android.content.res.Configuration
import android.util.Log
import android.webkit.WebSettings.TextSize
import androidx.compose.material3.TextField
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import java.util.Stack
import com.example.mad_assignmen01_connectfour.ui.theme.MAD_Assignmen01_ConnectFourTheme


@Composable
fun GameControls(
    navController: NavHostController,
    isSinglePlayer: Boolean,
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
    Row(
        modifier = Modifier.padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                navController.navigate(Routes.MAIN_MENU)
            }
        ) {
            Text(text = "Return to main menu")
        }

        Button(
            onClick = {
                if (isSinglePlayer) {
                    navController.navigate(Routes.GAME_START_MENU_1P)
                } else {
                    navController.navigate(Routes.GAME_START_MENU_2P)
                }
            }
        ) {
            Text(text = "Change Game settings")
        }
    }
}




@Composable
fun Connect4Board(
    rows: Int = 7,
    columns: Int = 8,
    isSinglePlayer: Boolean,
    gameViewModel: GameViewModel,
    navController: NavHostController
) {
    val board = gameViewModel.board.value
    val currentPlayer = gameViewModel.currentPlayer
    val gameMessage = gameViewModel.gameMessage
    val gameOver = gameViewModel.gameOver
    val moveStack = gameViewModel.moveStack
    val ai = Connect4AI()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    Log.d("Connect4Board", "Board state rows: ${board.boardState.size}, columns: ${board.boardState[0].size}")

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
                        state = board.boardState[row][col],
                        onClick = {
                            if (!gameOver.value) {
                                handleCellClick(
                                    row,
                                    col,
                                    board,
                                    currentPlayer.value,
                                    moveStack
                                ) { updatedBoard, nextPlayer ->
                                    gameViewModel.saveMove()
                                    gameViewModel.board.value = updatedBoard
                                    gameViewModel.handleWin()

                                    if (!gameOver.value) {
                                        currentPlayer.value = nextPlayer
                                    }

                                    if (isSinglePlayer && currentPlayer.value == 2 && !gameOver.value) {
                                        val aiMove = ai.getMove(board.boardState)
                                        if (aiMove != -1) {
                                            handleCellClick(
                                                0,
                                                aiMove,
                                                board,
                                                2,
                                                moveStack
                                            ) { updatedBoard, nextPlayer ->
                                                gameViewModel.board.value = updatedBoard
                                                gameViewModel.handleWin()

                                                if (!gameOver.value) {
                                                    currentPlayer.value = nextPlayer
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
            text = "Current Turn: Player ${currentPlayer.value}",
            modifier = Modifier.padding(top = 16.dp)
        )

        if (gameMessage.value.isNotEmpty()) {
            Text(
                text = gameMessage.value,
                modifier = Modifier.padding(top = 16.dp),
                color = Color.Red
            )
        }

        if (!isLandscape) {
            GameControls(
                navController =navController,
                isSinglePlayer = isSinglePlayer,
                moveStack = moveStack,
                onUndo = { previousBoard, newCurrentPlayer, message ->
                    gameViewModel.undoMove()
                },
                onReset = { rows, columns ->
                    gameViewModel.resetBoard(rows, columns)
                },
                rows = rows,
                columns = columns,
                currentPlayer = currentPlayer,
                gameMessage = gameMessage,
                gameOver = gameOver
            )
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

/***********************************************************************************/
// Purely in place for preview reasons, cant preview unless you have default vals
/**********************************************************************************/
@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    val navController = rememberNavController()

    GameScreen(
        navController = navController,
        isSinglePlayer = true,
        gridWidth = 7,
        gridHeight = 6
    )
}
/***********************************************************/
@Composable
fun GameScreen(
    navController: NavHostController,
    shVm: ConnectFourViewModel = ConnectFourViewModel(),
    isSinglePlayer: Boolean = false,
    gridWidth: Int = 7,
    gridHeight: Int = 6,
    player1Name: String = if (isSinglePlayer) shVm.singlePlayerProfileSelection.name else shVm.twoPlayerProfileSelectionP1.name,
    player2Name: String = if (isSinglePlayer) shVm.computerProfile.name else shVm.twoPlayerProfileSelectionP2.name
) {
    val player1 = if (isSinglePlayer) shVm.singlePlayerProfileSelection
                    else shVm.twoPlayerProfileSelectionP1
    val player2 = if (isSinglePlayer) shVm.computerProfile
                    else shVm.twoPlayerProfileSelectionP2
    val gameViewModel: GameViewModel = viewModel()
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
            Connect4Board(gridHeight, gridWidth, isSinglePlayer = isSinglePlayer, gameViewModel, navController)
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