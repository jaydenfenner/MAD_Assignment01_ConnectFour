package com.example.mad_assignmen01_connectfour

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun MainMenu(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Main Menu")
        Button(
            onClick = { navController.navigate("start/2player") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start 2 Player Game")
        }
        Button(
            onClick = { navController.navigate("start/1player") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start 1 Player Game")
        }
    }
}

@Composable
fun GameStartMenu(navController: NavHostController, isSinglePlayer: Boolean) {
    var player1Name by remember { mutableStateOf("Player 1") }
    var player2Name by remember { mutableStateOf("Player 2") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = if (isSinglePlayer) "Single Player Game" else "Two Player Game")

        TextField(
            value = player1Name,
            onValueChange = { player1Name = it },
            label = { Text("Enter Player 1 Name") }
        )

        if (!isSinglePlayer) {
            TextField(
                value = player2Name,
                onValueChange = { player2Name = it },
                label = { Text("Enter Player 2 Name") }
            )
        }
        Button(
            onClick = {
                if (isSinglePlayer) {
                    navController.navigate("connect4/1player/7/6/${player1Name}")
                } else {
                    navController.navigate("connect4/2player/7/6/${player1Name}/${player2Name}")
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start Standard Game (7x6)")
        }

        Button(
            onClick = {
                if (isSinglePlayer) {
                    navController.navigate("connect4/1player/6/5/${player1Name}")
                } else {
                    navController.navigate("connect4/2player/6/5/${player1Name}/${player2Name}")
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start Small Game (6x5)")
        }

        Button(
            onClick = {
                if (isSinglePlayer) {
                    navController.navigate("connect4/1player/8/7/${player1Name}")
                } else {
                    navController.navigate("connect4/2player/8/7/${player1Name}/${player2Name}")
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start Large Game (8x7)")
        }
    }
}