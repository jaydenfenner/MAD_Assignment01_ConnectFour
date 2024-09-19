package com.example.mad_assignmen01_connectfour

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val player1 = if (isSinglePlayer) shVm.singlePlayerProfileSelection
            else shVm.twoPlayerProfileSelectionP1
    val player2 = if (isSinglePlayer) shVm.computerProfile
            else shVm.twoPlayerProfileSelectionP2
    val gameVm = viewModel<GameViewModel>()
    gameVm.initialise(
        boardWidth = gridWidth,
        boardHeight = gridHeight,
        is1P = isSinglePlayer,
        p1_profile = player1,
        p2_profile = player2,
    )

    val orientation = LocalConfiguration.current.orientation
    InsetContent {
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT ->
                GameScreen_Portrait(
                    navController = navController,
                    shVm = shVm, gameVm = gameVm
                )
            else ->
                GameScreen_Landscape(
                    navController = navController,
                    shVm = shVm, gameVm = gameVm
                )
        }
    }
}
@Composable
fun GameScreen_Portrait(
    navController: NavHostController,
    shVm: ConnectFourViewModel,
    gameVm: GameViewModel,
) {
    val currentPlayerProfile = when (gameVm.currentPlayer) {
        1 -> gameVm.p1Profile
        else -> gameVm.p2Profile
    }
    Column(
        modifier = Modifier
            .background(color = Color.LightGray)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileDisplay(shVm = shVm, gameVm = gameVm)
//        Row() {
//            Column() {
//                Text(gameVm.p1Profile.name)
//                ProfileStatistics(gameVm.p1Profile)
//            }
//            Spacer(Modifier.size(50.dp))
//            Column(){
//                Text(gameVm.p2Profile.name)
//                ProfileStatistics(gameVm.p2Profile)
//            }
//        }
        DisplayGameMessage(gameVm)
        Text(text = gameVm.getPosition().boardString())
        Connect4Board(shVm, gameVm)
        Text(
            text = "Current Turn: ${currentPlayerProfile.name}",
            modifier = Modifier.padding(top = 16.dp)
        )
        CurrentGameData(gameVm)
        DisplayGameMessage(gameVm)
        GameControls(gameVm = gameVm, navController = navController)
    }
}
@Composable
fun GameScreen_Landscape(
    navController: NavHostController,
    shVm: ConnectFourViewModel,
    gameVm: GameViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray),
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileSelectorGridItem(
                selectedColor = shVm.leftPlayerDiskColour,
                isSelected = (gameVm.currentPlayer == 1),
                thisItemProfile = gameVm.p1Profile,
                onClick = {}
            )
            ProfileStatistics(gameVm.p1Profile)
        }
        Column(
            Modifier
                .fillMaxHeight()
                .weight(8f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally){
            DisplayGameMessage(gameVm)
            Connect4Board(shVm, gameVm)
            DisplayGameMessage(gameVm)
            CurrentGameData(gameVm)
            GameControls(gameVm = gameVm, navController = navController)
        }
        Column(
            Modifier
                .fillMaxHeight()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileSelectorGridItem(
                selectedColor = shVm.rightPlayerDiskColour,
                isSelected = (gameVm.currentPlayer == 2),
                thisItemProfile = gameVm.p2Profile,
                onClick = {}
            )
            ProfileStatistics(gameVm.p2Profile)
        }
    }
}

// **********************************************************************************
// Previews:         (Ignoring large screens for now)
// **********************************************************************************
@Composable
fun GameScreenPortrait_Preview() {
    val navController = rememberNavController()
    val shVm = viewModel<ConnectFourViewModel>()
    val gameVm = viewModel<GameViewModel>()
    gameVm.initialise(
        boardWidth = 7,
        boardHeight = 6,
        is1P = true,
        p1_profile = shVm.player1Profile,
        p2_profile = shVm.computerProfile,
    )
    GameScreen_Portrait(navController = navController,
        shVm = shVm, gameVm = gameVm)
}


@Composable
fun GameScreenLandscape_Preview() {
    val navController = rememberNavController()
    val shVm = viewModel<ConnectFourViewModel>()
    val gameVm = viewModel<GameViewModel>()
    gameVm.initialise(
        boardWidth = 7,
        boardHeight = 6,
        is1P = true,
        p1_profile = shVm.player1Profile,
        p2_profile = shVm.computerProfile,
    )
    GameScreen_Landscape(navController = navController,
        shVm = shVm, gameVm = gameVm)
}
@Preview(name = "5-inch Device Portrait",
    widthDp = previewWidthDp, heightDp = previewHeightDp, showBackground = true)
@Composable
fun Preview5Inch_GameScreen() {
    GameScreenPortrait_Preview()
}
@Preview(name = "5-inch Device Landscape",
    widthDp = previewHeightDp, heightDp = previewWidthDp, showBackground = true)
@Composable
fun Preview5InchLand_GameScreen() {
    GameScreenLandscape_Preview()
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