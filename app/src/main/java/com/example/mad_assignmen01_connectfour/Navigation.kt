package com.example.mad_assignmen01_connectfour

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

data object Routes{
    const val MAIN_MENU = "mainMenu"
    const val EDIT_PROFILES = "editProfiles"
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
        composable(Routes.EDIT_PROFILES) { EditProfilesScreen(sharedViewModel) }
        composable(Routes.GAME_START_MENU_1P) { Start1PGameScreen(navController, sharedViewModel) }
        composable(Routes.GAME_START_MENU_2P) { Start2PGameScreen(navController, sharedViewModel) }
        composable(Routes.GAME_1P + "/{gridWidth}/{gridHeight}/{player1Name}") { backStackEntry ->
            val gridWidth = backStackEntry.arguments?.getString("gridWidth")?.toInt() ?: 7
            val gridHeight = backStackEntry.arguments?.getString("gridHeight")?.toInt() ?: 6
            GameScreen(isSinglePlayer = true, gridWidth = gridWidth, gridHeight = gridHeight)
        }
        composable(Routes.GAME_2P + "/{gridWidth}/{gridHeight}/{player1Name}/{player2Name}") { backStackEntry ->
            val gridWidth = backStackEntry.arguments?.getString("gridWidth")?.toInt() ?: 7
            val gridHeight = backStackEntry.arguments?.getString("gridHeight")?.toInt() ?: 6
            GameScreen(isSinglePlayer = false, gridWidth = gridWidth, gridHeight = gridHeight)
        }
    }
}