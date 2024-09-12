package com.example.mad_assignmen01_connectfour

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mad_assignmen01_connectfour.ui.theme.MAD_Assignmen01_ConnectFourTheme

@Composable
fun MainMenuScreen(navController: NavHostController, shVm: ConnectFourViewModel) {
    val orientation = LocalConfiguration.current.orientation
    when (orientation) {
        Configuration.ORIENTATION_PORTRAIT ->
            MainMenu_Portrait(navController = navController, shVm = shVm)
        else ->
//            MenuLandscape()
            MainMenu_Portrait(navController = navController, shVm = shVm)
    }
}

@Composable
fun MainMenu_Portrait(navController: NavHostController, shVm: ConnectFourViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
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