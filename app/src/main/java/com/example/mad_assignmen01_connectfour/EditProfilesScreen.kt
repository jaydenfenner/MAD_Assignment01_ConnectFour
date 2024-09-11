package com.example.mad_assignmen01_connectfour

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun EditProfilesScreen(navController: NavHostController, shVm: ConnectFourViewModel) {
    val orientation = LocalConfiguration.current.orientation
    when (orientation) {
        Configuration.ORIENTATION_PORTRAIT ->
            EditProfiles_Portrait(navController = navController, shVm = shVm)
        else ->
//            EditProfiles_Landscape()
            EditProfiles_Portrait(navController = navController, shVm = shVm)
    }
}

// TODO create new profile
// TODO select profile to edit
// TODO choose name
// TODO choose avatar
// TODO choose colour

@Composable
fun EditProfiles_Portrait(navController: NavHostController, shVm: ConnectFourViewModel) {
    var selectedProfile by remember {mutableStateOf<UserProfile?>(null)}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (selectedProfile == null) {
            Text("Please select a profile to edit")
            // TODO list profiles
            Button(onClick = {/* TODO */}) {
                Text(text = "Create New Profile")
            }
        } else {
            Text("profiles screen")
        }

    }
}

@Composable
fun EditProfiles_Landscape() {
    Column(Modifier.fillMaxSize()) {
        Text("Landscape")
    }
}

// **********************************************************************************
// Previews:         (Ignoring large screens for now)
// **********************************************************************************
@Composable
fun EditProfilesScreen_Preview() {
    val navController = rememberNavController()
    val shVm = viewModel<ConnectFourViewModel>()
    EditProfilesScreen(navController, shVm)
}

@Preview(name = "5-inch Device Portrait",
    widthDp = previewWidthDp, heightDp = previewHeightDp, showBackground = true)
@Composable
fun EditProfilesScreen_Preview5Inch() {
    EditProfilesScreen_Preview()
}
//@Preview(name = "5-inch Device Landscape",
//    widthDp = previewHeightDp, heightDp = previewWidthDp, showBackground = true)
//@Composable
//fun EditProfilesScreen_Preview5InchLand() {
//    EditProfilesScreen_Preview()
//}

// TODO check if we need these, my friend got 100% with no tablet layouts
//@Preview(name = "10-inch Tablet Portrait",
//    widthDp = previewTabletWidthDp, heightDp = previewTabletHeightDp, showBackground = true)
//@Composable
//fun MainMenuScreen_Preview10InchTablet() {
//    EditProfilesScreen_Preview()
//}
//@Preview(name = "10-inch Tablet Landscape",
//    widthDp = previewTabletHeightDp, heightDp = previewTabletWidthDp, showBackground = true)
//@Composable
//fun MainMenuScreen_Preview10InchTabletLand() {
//    EditProfilesScreen_Preview()
//}