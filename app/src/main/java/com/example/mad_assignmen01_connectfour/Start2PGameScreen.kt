package com.example.mad_assignmen01_connectfour

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
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
fun Start2PGameScreen(navController: NavHostController, shVm: ConnectFourViewModel) {
    val orientation = LocalConfiguration.current.orientation
    InsetContent {
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT ->
                Start2PGame_Portrait(navController = navController, shVm = shVm)
            else ->
                Start2Game_Landscape(navController = navController, shVm = shVm)
        }
    }
}

@Composable
fun Start2PGame_Portrait(navController: NavHostController, shVm: ConnectFourViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Single Player Game")
        GamePlayerSelector(shVm = shVm, selectedProfile = shVm.twoPlayerProfileSelectionP1,
            maxHeight = 1000.dp,
            defaultProfile = shVm.player1Profile,
            unavailableProfile = shVm.twoPlayerProfileSelectionP2,
            onProfileSelected = { shVm.twoPlayerProfileSelectionP1 = it },
            prompt = "Select Player 1 Profile",
            selectedColor = shVm.leftPlayerDiskColour,
        )
        GamePlayerSelector(shVm = shVm, selectedProfile = shVm.twoPlayerProfileSelectionP2,
            prompt = "Select Player 2 Profile",
            maxHeight = 1000.dp,
            defaultProfile = shVm.player2Profile,
            unavailableProfile = shVm.twoPlayerProfileSelectionP1,
            onProfileSelected = { shVm.twoPlayerProfileSelectionP2 = it },
            selectedColor = shVm.rightPlayerDiskColour
        )
        StartGameScreenButtons(
            modifier = Modifier.padding(bottom = 20.dp),
            onStartStandard = { navController.navigate(Routes.GAME_2P_STANDARD_7_6) },
            onStartSmall = { navController.navigate(Routes.GAME_2P_SMALL_6_5) },
            onStartLarge = { navController.navigate(Routes.GAME_2P_LARGE_8_7) },
        )
    }
}

@Composable
fun Start2Game_Landscape(navController: NavHostController, shVm: ConnectFourViewModel) {
    Column(Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Single Player Game")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GamePlayerSelector(shVm = shVm, selectedProfile = shVm.twoPlayerProfileSelectionP1,
                    maxHeight = 400.dp,
                    defaultProfile = shVm.player1Profile,
                    unavailableProfile = shVm.twoPlayerProfileSelectionP2,
                    onProfileSelected = { shVm.twoPlayerProfileSelectionP1 = it },
                    prompt = "Select Player 1 Profile",
                    selectedColor = shVm.leftPlayerDiskColour,
                )
                GamePlayerSelector(shVm = shVm, selectedProfile = shVm.twoPlayerProfileSelectionP2,
                    prompt = "Select Player 2 Profile",
                    maxHeight = 400.dp,
                    defaultProfile = shVm.player2Profile,
                    unavailableProfile = shVm.twoPlayerProfileSelectionP1,
                    onProfileSelected = { shVm.twoPlayerProfileSelectionP2 = it },
                    selectedColor = shVm.rightPlayerDiskColour
                )
            }
            StartGameScreenButtons(
                modifier = Modifier.weight(1f),
                onStartStandard = { navController.navigate(Routes.GAME_2P_STANDARD_7_6) },
                onStartSmall = { navController.navigate(Routes.GAME_2P_SMALL_6_5) },
                onStartLarge = { navController.navigate(Routes.GAME_2P_LARGE_8_7) },
            )
        }

    }
}

// **********************************************************************************
// Previews:         (Ignoring large screens for now)
// **********************************************************************************
@Composable
fun Start2PGame_Preview() {
    val navController = rememberNavController()
    val shVm = viewModel<ConnectFourViewModel>()
    shVm.userProfiles.add(UserProfile("test", AvatarIDs.POO_EMOJI))
    shVm.twoPlayerProfileSelectionP1 = shVm.userProfiles[0]
    Start2PGameScreen(navController, shVm)
}

@Preview(name = "5-inch Device Portrait",
    widthDp = previewWidthDp, heightDp = previewHeightDp, showBackground = true)
@Composable
fun Start2PGame_Preview5Inch() {
    Start2PGame_Preview()
}
@Preview(name = "5-inch Device Landscape",
    widthDp = previewHeightDp, heightDp = previewWidthDp, showBackground = true)
@Composable
fun Start2PGame_Preview5InchLand() {
    Start2PGame_Preview()
}

// TODO check if we need these, my friend got 100% with no tablet layouts
//@Preview(name = "10-inch Tablet Portrait",
//    widthDp = previewTabletWidthDp, heightDp = previewTabletHeightDp, showBackground = true)
//@Composable
//fun Start2PGame_Preview10InchTablet() {
//    Start2PGame_Preview()
//}
//@Preview(name = "10-inch Tablet Landscape",
//    widthDp = previewTabletHeightDp, heightDp = previewTabletWidthDp, showBackground = true)
//@Composable
//fun Start2PGame_Preview10InchTabletLand() {
//    Start2PGame_Preview()
//}