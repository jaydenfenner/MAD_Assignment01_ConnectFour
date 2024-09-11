package com.example.mad_assignmen01_connectfour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mad_assignmen01_connectfour.ui.theme.MAD_Assignmen01_ConnectFourTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MAD_Assignmen01_ConnectFourTheme {
                AppNavigation()
            }
        }
    }
}

const val previewWidthDp = 360
const val previewHeightDp = 740
const val previewTabletWidthDp = 800
const val previewTabletHeightDp = 1280

// **********************************************************************************
// Previews:         (Ignoring large screens for now)
// **********************************************************************************
@Composable
fun ConnectFourApp_Preview() {
    MAD_Assignmen01_ConnectFourTheme {
        AppNavigation()
    }
}

@Preview(name = "5-inch Device Portrait",
    widthDp = previewWidthDp, heightDp = previewHeightDp, showBackground = true)
@Composable
fun App_Preview5Inch() {
    ConnectFourApp_Preview()
}
@Preview(name = "5-inch Device Landscape",
    widthDp = previewHeightDp, heightDp = previewWidthDp, showBackground = true)
@Composable
fun App_Preview5InchLand() {
    ConnectFourApp_Preview()
}

// TODO check if we need these, my friend got 100% with no tablet layouts
@Preview(name = "10-inch Tablet Portrait",
    widthDp = previewTabletWidthDp, heightDp = previewTabletHeightDp, showBackground = true)
@Composable
fun App_Preview10InchTablet() {
    ConnectFourApp_Preview()
}
@Preview(name = "10-inch Tablet Landscape",
    widthDp = previewTabletHeightDp, heightDp = previewTabletWidthDp, showBackground = true)
@Composable
fun App_Preview10InchTabletLand() {
    ConnectFourApp_Preview()
}