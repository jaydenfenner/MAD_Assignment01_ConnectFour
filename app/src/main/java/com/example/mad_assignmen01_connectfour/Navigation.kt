package com.example.mad_assignmen01_connectfour

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

data object Routes{
    const val MAIN_MENU = "mainMenu"
    const val GAME_START_MENU_1P = "gameStart/1Player"
    const val GAME_START_MENU_2P = "gameStart/2Player"
    const val GAME_1P = "connect4/1player"
    const val GAME_2P = "connect4/2player"
}

@Composable
fun AppNavigation() {
    val sharedViewModel = viewModel<ConnectFourViewModel>()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.MAIN_MENU) {
        composable(Routes.MAIN_MENU) {MainMenuScreen(navController, sharedViewModel) }
        composable(Routes.GAME_START_MENU_1P) {
            GameStartMenu(navController, isSinglePlayer = true) }
        composable(Routes.GAME_START_MENU_2P) {
            GameStartMenu(navController, isSinglePlayer = false) }
        composable(Routes.GAME_1P) { DefaultPreview(isSinglePlayer = true) }
        composable(Routes.GAME_2P) { DefaultPreview(isSinglePlayer = false) }
    }
}