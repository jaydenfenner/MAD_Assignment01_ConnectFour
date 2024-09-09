package com.example.mad_assignmen01_connectfour

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mad_assignmen01_connectfour.ui.theme.MAD_Assignmen01_ConnectFourTheme

//Message Test
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MAD_Assignmen01_ConnectFourTheme {
                val dataViewModel = viewModel<ConnectFourViewModel>()
                Menu()
            }
        }
    }
}

@Composable
fun Menu() {
    val orientation = LocalConfiguration.current.orientation
    when (orientation) {
        Configuration.ORIENTATION_PORTRAIT -> MenuPortrait()
        else -> MenuLandscape()
    }
}

@Composable
fun MenuPortrait() {
    Column(Modifier.fillMaxSize()) {
        Text("Portrait")
    }
}

@Composable
fun MenuLandscape() {
    Column(Modifier.fillMaxSize()) {
        Text("Landscape")
    }
}

// **********************************************************************************
// Previews:         (Ignoring large screens for now)
// **********************************************************************************

@Preview(name = "5-inch Device Portrait", widthDp = 360, heightDp = 640,
    showBackground = true)
@Composable
fun MainContentBoxPreview5Inch() {
    MAD_Assignmen01_ConnectFourTheme {
        Menu()
    }
}

@Preview(name = "5-inch Device Landscape", widthDp = 640, heightDp = 360,
    showBackground = true)
@Composable
fun MainContentBoxPreview5InchLand() {
    MAD_Assignmen01_ConnectFourTheme {
        Menu()
    }
}

//@Preview(name = "10-inch Tablet Portrait", widthDp = 600, heightDp = 960,
//    showBackground = true)
//@Composable
//fun MainContentBoxPreview10InchTablet() {
//    MAD_Assignmen01_ConnectFourTheme {
//        Menu()
//    }
//}
//
//@Preview(name = "10-inch Tablet Landscape", widthDp = 960, heightDp = 600,
//    showBackground = true)
//@Composable
//fun MainContentBoxPreview10InchTabletLand() {
//    MAD_Assignmen01_ConnectFourTheme {
//        Menu()
//    }
//}