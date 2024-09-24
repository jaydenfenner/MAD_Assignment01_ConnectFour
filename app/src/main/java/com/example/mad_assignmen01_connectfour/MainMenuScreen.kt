package com.example.mad_assignmen01_connectfour

import android.content.res.Configuration
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mad_assignmen01_connectfour.ui.theme.MAD_Assignmen01_ConnectFourTheme

@Composable
fun MainMenuScreen(navController: NavHostController, shVm: ConnectFourViewModel) {
    val orientation = LocalConfiguration.current.orientation
    InsetContent {
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT ->
                MainMenu_Portrait(navController = navController, shVm = shVm)
            else ->
                MainMenu_Portrait(navController = navController, shVm = shVm)
        }
    }
}

@Composable
fun MainMenu_Portrait(navController: NavHostController, shVm: ConnectFourViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Main Menu")
        Button(
            onClick = { navController.navigate(Routes.START_GAME_MENU_1P) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start 1 Player Game")
        }
        Button(
            onClick = { navController.navigate(Routes.START_GAME_MENU_2P) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start 2 Player Game")
        }
        Button(
            onClick = { navController.navigate(Routes.EDIT_PROFILES) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Edit Profiles")
        }
        Button(
            onClick = { navController.navigate(Routes.CHANGE_DISK_COLORS) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Change Player Disk Colours")
        }
        Button(
            onClick = { navController.navigate(Routes.GAME_AI_AS_P1) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Single Player with AI player 1")
        }

        Text("Select AI Difficulty:", modifier = Modifier.padding(top = 16.dp))
        Row {
            Button(onClick = {shVm.aiDifficulty = AiDifficulty.TRIVIAL},
                enabled = shVm.aiDifficulty != AiDifficulty.TRIVIAL
            ) {
                Text("Trivial", fontSize = 15.sp)
            }
            Button(onClick = {shVm.aiDifficulty = AiDifficulty.EASY},
                enabled = shVm.aiDifficulty != AiDifficulty.EASY
            ) {
                Text("Easy", fontSize = 15.sp)
            }
        }
        Row {
            Button(onClick = {shVm.aiDifficulty = AiDifficulty.MEDIUM},
                enabled = shVm.aiDifficulty != AiDifficulty.MEDIUM
            ) {
                Text("Medium", fontSize = 15.sp)
            }
            Button(onClick = {shVm.aiDifficulty = AiDifficulty.HARD},
                enabled = shVm.aiDifficulty != AiDifficulty.HARD
            ) {
                Text("Hard", fontSize = 15.sp)
            }
        }
        Button(onClick = {shVm.aiDifficulty = AiDifficulty.IMPOSSIBLE},
            enabled = shVm.aiDifficulty != AiDifficulty.IMPOSSIBLE
        ) {
            Text("IMPOSSIBLE", fontSize = 15.sp)
        }

    }
}

@Composable
fun MainMenu_Landscape() {
    Column(Modifier.fillMaxSize()) {
        Text("Landscape")
    }
}

// **********************************************************************************
// Previews:         (Ignoring large screens for now)
// **********************************************************************************
@Composable
fun MainMenuScreen_Preview() {
    val navController = rememberNavController()
    val shVm = viewModel<ConnectFourViewModel>()
    MAD_Assignmen01_ConnectFourTheme {
        MainMenuScreen(navController = navController, shVm = shVm)
    }
}

@Preview(name = "5-inch Device Portrait",
    widthDp = previewWidthDp, heightDp = previewHeightDp, showBackground = true)
@Composable
fun MainMenuScreen_Preview5Inch() {
    MainMenuScreen_Preview()
}
@Preview(name = "5-inch Device Landscape",
    widthDp = previewHeightDp, heightDp = previewWidthDp, showBackground = true)
@Composable
fun MainMenuScreen_Preview5InchLand() {
    MainMenuScreen_Preview()
}

// TODO check if we need these, my friend got 100% with no tablet layouts
//@Preview(name = "10-inch Tablet Portrait",
//    widthDp = previewTabletWidthDp, heightDp = previewTabletHeightDp, showBackground = true)
//@Composable
//fun MainMenuScreen_Preview10InchTablet() {
//    MainMenuScreen_Preview()
//}
//@Preview(name = "10-inch Tablet Landscape",
//    widthDp = previewTabletHeightDp, heightDp = previewTabletWidthDp, showBackground = true)
//@Composable
//fun MainMenuScreen_Preview10InchTabletLand() {
//    MainMenuScreen_Preview()
//}