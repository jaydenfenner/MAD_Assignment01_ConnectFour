package com.example.mad_assignmen01_connectfour

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun Start1PGameScreen(navController: NavHostController, shVm: ConnectFourViewModel) {
    val orientation = LocalConfiguration.current.orientation
    InsetContent {
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT ->
                Start1PGame_Portrait(navController = navController, shVm = shVm)
            else ->
                Start1PGame_Landscape(navController = navController, shVm = shVm)
        }
    }
}

@Composable
fun Start1PGame_Portrait(navController: NavHostController, shVm: ConnectFourViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Single Player Game")
        GamePlayerSelector(shVm = shVm, selectedProfile = shVm.singlePlayerProfileSelection,
            defaultProfile = shVm.player1Profile,
            onProfileSelected = {
                shVm.singlePlayerProfileSelection = it
            },
        )
        StartGameScreenButtons(
            onStartStandard = { navController.navigate("connect4/1player/7/6/") },
            onStartSmall = { navController.navigate("connect4/1player/6/5/}") },
            onStartLarge = { navController.navigate("connect4/1player/8/7/") },
        )
    }
}

@Composable
fun Start1PGame_Landscape(navController: NavHostController, shVm: ConnectFourViewModel) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Text(text = "Single Player Game")
        Row {
            Column(Modifier.weight(1f)) {
                GamePlayerSelector(shVm = shVm, selectedProfile = shVm.singlePlayerProfileSelection,
                    defaultProfile = shVm.player1Profile,
                    onProfileSelected = {
                        shVm.singlePlayerProfileSelection = it
                    },
                )
            }
            StartGameScreenButtons(modifier = Modifier.weight(1f),
                onStartStandard = { navController.navigate("connect4/1player/7/6/") },
                onStartSmall = { navController.navigate("connect4/1player/6/5/}") },
                onStartLarge = { navController.navigate("connect4/1player/8/7/") },
            )
        }
    }
}

// **********************************************************************************
// Previews:         (Ignoring large screens for now)
// **********************************************************************************
@Composable
fun Start1PGame_Preview() {
    val navController = rememberNavController()
    val shVm = viewModel<ConnectFourViewModel>()
    Start1PGameScreen(navController, shVm)
}

@Preview(name = "5-inch Device Portrait",
    widthDp = previewWidthDp, heightDp = previewHeightDp, showBackground = true)
@Composable
fun Start1PGame_Preview5Inch() {
    Start1PGame_Preview()
}
@Preview(name = "5-inch Device Landscape",
    widthDp = previewHeightDp, heightDp = previewWidthDp, showBackground = true)
@Composable
fun Start1PGame_Preview5InchLand() {
    Start1PGame_Preview()
}

// TODO check if we need these, my friend got 100% with no tablet layouts
//@Preview(name = "10-inch Tablet Portrait",
//    widthDp = previewTabletWidthDp, heightDp = previewTabletHeightDp, showBackground = true)
//@Composable
//fun Start1PGame_Preview10InchTablet() {
//    Start1PGame_Preview()
//}
//@Preview(name = "10-inch Tablet Landscape",
//    widthDp = previewTabletHeightDp, heightDp = previewTabletWidthDp, showBackground = true)
//@Composable
//fun Start1PGame_Preview10InchTabletLand() {
//    Start1PGame_Preview()
//}