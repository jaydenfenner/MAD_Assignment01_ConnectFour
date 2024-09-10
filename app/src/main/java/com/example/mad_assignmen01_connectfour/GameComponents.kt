package com.example.mad_assignmen01_connectfour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mad_assignmen01_connectfour.ui.theme.MAD_Assignmen01_ConnectFourTheme


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") { MainMenu(navController) }
        composable("connect4") { DefaultPreview() }
    }
}

@Composable
fun MainMenu(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Main Menu")
        Button(
            onClick = { navController.navigate("connect4") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Start Connect 4")
        }
    }
}

@Composable
fun Connect4Board(rows: Int = 6, columns: Int = 7) {
    Box(
        modifier = Modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(color = Color.LightGray)
                .padding(16.dp)
        ) {
            Column {
                for (row in 0 until rows) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (col in 0 until columns) {
                            Connect4Cell(
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(50.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Connect4Cell(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color.Blue)
            .border(2.dp, Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Circle(color = Color.White)
    }
}

@Composable
fun Circle(color: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color, shape = CircleShape)
    )
}

// Only display static content in previews
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MAD_Assignmen01_ConnectFourTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            profileDisplay()
            Connect4Board()
        }
    }
}

@Composable
fun profileDisplay() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(75.dp)
            ) {
                Text(text = "")
            }
            Text(text = "Player 1")
        }

        Spacer(modifier = Modifier.width(100.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(75.dp)
            ) {
                Text(text = "")
            }
            Text(text = "Player 2")
        }
    }
}
