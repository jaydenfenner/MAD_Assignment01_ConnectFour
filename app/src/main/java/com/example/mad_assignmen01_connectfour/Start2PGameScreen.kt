package com.example.mad_assignmen01_connectfour

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
fun Start2PGameScreen(navController: NavHostController, shVm: ConnectFourViewModel) {
    val orientation = LocalConfiguration.current.orientation
    when (orientation) {
        Configuration.ORIENTATION_PORTRAIT ->
            Start2PGame_Portrait(navController = navController, shVm = shVm)
        else ->
//            Start2PGame_Landscape()
            Start2PGame_Portrait(navController = navController, shVm = shVm)
    }
}

@Composable
fun Start2PGame_Portrait(navController: NavHostController, shVm: ConnectFourViewModel) {
    var player1Name by remember { mutableStateOf("Player 1") }
    var player2Name by remember { mutableStateOf("Player 2") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Two Player Game")

        TextField(
            value = player1Name,
            onValueChange = { player1Name = it },
            label = { Text("Enter Player 1 Name") }
        )
        TextField(
            value = player2Name,
            onValueChange = { player2Name = it },
            label = { Text("Enter Player 2 Name") }
        )

        Button(
            onClick = {
                navController.navigate("connect4/2player/7/6/${player1Name}/${player2Name}")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start Standard Game (7x6)")
        }

        Button(
            onClick = {
                navController.navigate("connect4/2player/6/5/${player1Name}/${player2Name}")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start Small Game (6x5)")
        }

        Button(
            onClick = {
                navController.navigate("connect4/2player/8/7/${player1Name}/${player2Name}")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start Large Game (8x7)")
        }
    }
}

@Composable
fun Start2PGame_Landscape() {
    Column(Modifier.fillMaxSize()) {
        Text("Landscape")
    }
}

// **********************************************************************************
// Previews:         (Ignoring large screens for now)
// **********************************************************************************
@Composable
fun Start2PGame_Preview() {
    val navController = rememberNavController()
    val shVm = viewModel<ConnectFourViewModel>()
    Start2PGameScreen(navController, shVm)
}

@Preview(name = "5-inch Device Portrait",
    widthDp = previewWidthDp, heightDp = previewHeightDp, showBackground = true)
@Composable
fun Start2PGame_Preview5Inch() {
    Start2PGame_Preview()
}
//@Preview(name = "5-inch Device Landscape",
//    widthDp = previewHeightDp, heightDp = previewWidthDp, showBackground = true)
//@Composable
//fun Start2PGame_Preview5InchLand() {
//    Start2PGame_Preview()
//}

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