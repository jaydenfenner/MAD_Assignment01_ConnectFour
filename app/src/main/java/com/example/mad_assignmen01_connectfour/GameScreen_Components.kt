package com.example.mad_assignmen01_connectfour

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.content.res.Configuration
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import java.util.Stack

/**
 * set the game message and game over states
 */
@Composable
fun GameControls(
    gameVm: GameViewModel,
    navController: NavHostController,
    is1P: Boolean
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
        Text(text = "Reset Board")
    }

    Row(
        modifier = Modifier.padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Adds space between buttons
    ) {
        Button(
            onClick = { navController.navigate(Routes.MAIN_MENU) }
        ) {
            Text(text = "Main Menu")
        }

        Button(
            onClick = { if(is1P) {
                navController.navigate(Routes.START_GAME_MENU_1P)
            }
            else{
                navController.navigate(Routes.START_GAME_MENU_2P)
            }
            }
        ) {
            Text(text = "Board Settings")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_Connect4Board() {
    val gameVm = viewModel<GameViewModel>()
    val navController = rememberNavController()
    gameVm.initialise(
        boardWidth = 7,
        boardHeight = 6,
        is1P = true
    )
    Connect4Board(gameVm, navController = navController)
}

@Composable
fun Connect4Board(
    gameVm: GameViewModel,
    navController: NavHostController
) {
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
                        playerNumber = gameVm.board.boardState[y][x],
                        onClick = {
                            /** only allow click if game not over */
                            if (!gameVm.isGameOver) {
                                gameVm.makePlayerMove(col = x)

                                /** make AI move if single player */
                                if (gameVm.isSinglePlayer
                                    && gameVm.currentPlayer == 2
                                    && !gameVm.isGameOver
                                    ) {
                                    gameVm.makeAIMove()
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

        GameControls(gameVm = gameVm, is1P = gameVm.isSinglePlayer, navController = navController)
    }
}

@Composable
fun Connect4Cell(playerNumber: Int, onClick: () -> Unit) {
    val context = LocalContext.current
    val metrics = context.resources.displayMetrics
    val screenWidth = metrics.widthPixels / metrics.density
    val screenHeight = metrics.heightPixels / metrics.density

    val cellSize = if (screenWidth > screenHeight) {
        (screenHeight / 13).dp
    } else {
        (screenWidth / 11).dp
    }

    val color = when (playerNumber) {
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

@Preview(showBackground = true)
@Composable
fun Preview_ProfileDisplay() {
    val vm = viewModel<ConnectFourViewModel>()
    val gvm = viewModel<GameViewModel>()
    ProfileDisplay(
        leftProfile = vm.player1Profile,
        rightProfile = vm.computerProfile,
        gameVm = gvm
    )
}

@Composable
fun ProfileDisplay(gameVm: GameViewModel,
    leftProfile: UserProfile, rightProfile: UserProfile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        ProfileSelectorGridItem(
            isSelected = (gameVm.currentPlayer == 1),
            userProfile = leftProfile,
            onClick = {}
        )

        Spacer(modifier = Modifier.width(100.dp))

        ProfileSelectorGridItem(
            isSelected = (gameVm.currentPlayer == 2),
            userProfile = rightProfile,
            onClick = {}
        )
    }
}