package com.example.mad_assignmen01_connectfour

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun GameScreen(navController: NavHostController, shVm: ConnectFourViewModel,
               isSinglePlayer: Boolean,
               gridWidth: Int, gridHeight: Int
) {
    // initialise game view model and set board dims
    val gameVm = viewModel<GameViewModel>()
    gameVm.initialise(
        boardWidth = gridWidth,
        boardHeight = gridHeight,
        is1P = isSinglePlayer
    )

    val orientation = LocalConfiguration.current.orientation
    InsetContent {
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT ->
                GameScreen_Portrait(navController = navController,
                    shVm = shVm, gameVm = gameVm)
            else ->
                GameScreen_Portrait(navController = navController,
                    shVm = shVm, gameVm = gameVm)
        }
    }
}

@Composable
fun GameScreen_Portrait(
    navController: NavHostController,
    shVm: ConnectFourViewModel,
    gameVm: GameViewModel,
) {
    val player1 = if (gameVm.isSinglePlayer) shVm.singlePlayerProfileSelection
                    else shVm.twoPlayerProfileSelectionP1
    val player2 = if (gameVm.isSinglePlayer) shVm.computerProfile
                    else shVm.twoPlayerProfileSelectionP2
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
        Connect4Board(gameVm = gameVm)
    }
}

// **********************************************************************************
// Previews:         (Ignoring large screens for now)
// **********************************************************************************
@Composable
fun GameScreen_Preview() {
    val navController = rememberNavController()
    val shVm = viewModel<ConnectFourViewModel>()
    val gameVm = viewModel<GameViewModel>()
    gameVm.initialise(
        boardWidth = 7,
        boardHeight = 6,
        is1P = true
    )
    GameScreen_Portrait(navController = navController,
        shVm = shVm, gameVm = gameVm)
}

@Preview(name = "5-inch Device Portrait",
    widthDp = previewWidthDp, heightDp = previewHeightDp, showBackground = true)
@Composable
fun Preview5Inch_GameScreen() {
    GameScreen_Preview()
}
@Preview(name = "5-inch Device Landscape",
    widthDp = previewHeightDp, heightDp = previewWidthDp, showBackground = true)
@Composable
fun Preview5InchLand_GameScreen() {
    GameScreen_Preview()
}

// TODO check if we need these, my friend got 100% with no tablet layouts
//@Preview(name = "10-inch Tablet Portrait",
//    widthDp = previewTabletWidthDp, heightDp = previewTabletHeightDp, showBackground = true)
//@Composable
//fun Start1PGame_Preview10InchTablet() {
//    GameScreen_Preview()
//}
//@Preview(name = "10-inch Tablet Landscape",
//    widthDp = previewTabletHeightDp, heightDp = previewTabletWidthDp, showBackground = true)
//@Composable
//fun Start1PGame_Preview10InchTabletLand() {
//    GameScreen_Preview()
//}