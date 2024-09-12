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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Stack
import com.example.mad_assignmen01_connectfour.ui.theme.MAD_Assignmen01_ConnectFourTheme

/**
 * set the game message and game over states
 */
@Composable
fun GameControls(
    gameVm: GameViewModel,
) {
    Button(
        onClick = {gameVm.undoMove()},
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(text = "Undo Move")
    }

    Button(
        onClick = {gameVm.resetBoard()},
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(text = "Reset Game")
    }
}

@Composable
fun Connect4Board(
    gameVm: GameViewModel,
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
        for (y in 0 until gameVm.height) {
            Row {
                for (x in 0 until gameVm.width) {
                    Connect4Cell(
                        state = gameVm.board.boardState[y][x],
                        onClick = {
                            if (!gameVm.isGameOver) {
                                handleCellClick(
                                    row = y, col = x,
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

        if (!isLandscape) {
            GameControls(gameVm = gameVm)
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