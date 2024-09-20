package com.example.mad_assignmen01_connectfour

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

data object Routes{
    const val MAIN_MENU = "mainMenu"
    const val EDIT_PROFILES = "editProfiles"
    const val CHANGE_DISK_COLORS = "changeDiskColors"
    const val START_GAME_MENU_1P = "gameStart/1Player"
    const val START_GAME_MENU_2P = "gameStart/2Player"
    const val GAME_1P_STANDARD_7_6 = "connect4/1player/standard"
    const val GAME_1P_SMALL_6_5 = "connect4/1player/small"
    const val GAME_1P_LARGE_8_7 = "connect4/1player/Large"
    const val GAME_2P_STANDARD_7_6 = "connect4/2player/standard"
    const val GAME_2P_SMALL_6_5 = "connect4/2player/small"
    const val GAME_2P_LARGE_8_7 = "connect4/2player/Large"

    const val GAME_AI_AS_P1 = "game/aiAsPlayer1"
}

@Composable
fun AppNavigation() {
    val sharedViewModel = viewModel<ConnectFourViewModel>()
    val navController = rememberNavController()

//    NavHost(navController = navController, startDestination = Routes.MAIN_MENU) {
//    NavHost(navController = navController, startDestination = Routes.GAME_1P_SMALL_6_5) {
    NavHost(navController = navController, startDestination = Routes.GAME_AI_AS_P1) { // TODO extra option for AI as P1

        composable(Routes.MAIN_MENU) {MainMenuScreen(navController, sharedViewModel) }
        composable(Routes.EDIT_PROFILES) { EditProfilesScreen(sharedViewModel) }
        composable(Routes.CHANGE_DISK_COLORS) { ChangeDisksScreen(sharedViewModel) }
        composable(Routes.START_GAME_MENU_1P) { Start1PGameScreen(navController, sharedViewModel) }
        composable(Routes.START_GAME_MENU_2P) { Start2PGameScreen(navController, sharedViewModel) }

        // Single Player
        composable(Routes.GAME_1P_STANDARD_7_6) {
            GameScreen(navController,sharedViewModel,
                isSinglePlayer = true, gridWidth = 7, gridHeight = 6)
        }
        composable(Routes.GAME_1P_SMALL_6_5) {
            GameScreen(navController,sharedViewModel,
                isSinglePlayer = true, gridWidth = 6, gridHeight = 5)
        }
        composable(Routes.GAME_1P_LARGE_8_7) {
            GameScreen(navController,sharedViewModel,
                isSinglePlayer = true, gridWidth = 8, gridHeight = 7)
        }

        // Two Player
        composable(Routes.GAME_2P_STANDARD_7_6) {
            GameScreen(navController,sharedViewModel,
                isSinglePlayer = false, gridWidth = 7, gridHeight = 6)
        }
        composable(Routes.GAME_2P_SMALL_6_5) {
            GameScreen(navController,sharedViewModel,
                isSinglePlayer = false, gridWidth = 6, gridHeight = 5)
        }
        composable(Routes.GAME_2P_LARGE_8_7) {
            GameScreen(navController,sharedViewModel,
                isSinglePlayer = false, gridWidth = 8, gridHeight = 7)
        }

        /** EXTRA */
        composable(Routes.GAME_AI_AS_P1) { // TODO extra option for AI as P1
            GameScreen(navController,sharedViewModel,
                isSinglePlayer = true, gridWidth = 7, gridHeight = 6,
                isAIPlayer1 = true)
        }
    }
}