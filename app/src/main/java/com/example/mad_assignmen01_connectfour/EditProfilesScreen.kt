package com.example.mad_assignmen01_connectfour

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class EditProfilesViewModel: ViewModel() {
    var selectedProfile by mutableStateOf<UserProfile?>(null)
    var selectedAvatarID by mutableStateOf<Int?>(null)
}

@Composable
fun EditProfilesScreen(navController: NavHostController, shVm: ConnectFourViewModel) {
    val localVm = viewModel<EditProfilesViewModel>()
    localVm.selectedProfile = shVm.player1Profile // TODO need to remove later -------------
    localVm.selectedAvatarID = AvatarIDs.MONKEY // TODO need to remove later -------------
    val orientation = LocalConfiguration.current.orientation
    when (orientation) {
        Configuration.ORIENTATION_PORTRAIT ->
            EditProfiles_Portrait(navController = navController, shVm = shVm, localVm)
        else ->
//            EditProfiles_Landscape()
            EditProfiles_Landscape(navController = navController, shVm = shVm, localVm)
    }
}

// TODO create new profile
// TODO choose avatar
// TODO choose colour // MOVE TO GAME SCREEN

@Composable
fun EditProfiles_Portrait(navController: NavHostController, shVm: ConnectFourViewModel,
                          localVm: EditProfilesViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        ProfileSelector(shVm = shVm, localVm = localVm,
            onAddButtonClicked = {})

        Spacer(Modifier.size(10.dp))

        if (localVm.selectedProfile == null) {
            Text("Please select a profile to edit", fontSize = 20.sp)
        } else {
            Text("Customize selected profile:", fontSize = 20.sp)
        }
        TextField(
            value = localVm.selectedProfile?.name ?: "",
            onValueChange = { localVm.selectedProfile?.name = it },
            label = { Text("Profile Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Text("Choose Avatar:", fontSize = 20.sp)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 60.dp)
        ) {
            items(UserAvatarIDsList) { avatarID ->
                Image(
                    painter = painterResource(id = avatarID),
                    contentDescription = "",
                    modifier = Modifier
                        .background(
                            color = if (localVm.selectedAvatarID == avatarID) Color.Cyan
                                else Color.Transparent,
                            shape = CircleShape
                            )
                        .size(50.dp)
                        .clickable {}

                )
            }
        }
    }
}

@Composable
fun EditProfiles_Landscape(navController: NavHostController, shVm: ConnectFourViewModel,
                           localVm: EditProfilesViewModel) {
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        ) {
            ProfileSelector(shVm = shVm, localVm = localVm,
                onAddButtonClicked = {})
        }
        Column(Modifier.padding(16.dp).weight(1f)
        ) {
//            if (localVm.selectedProfile == null) {
//                Text("Please select a profile to edit", fontSize = 20.sp)
//            } else {
//                Text("Customize selected profile:", fontSize = 20.sp)
//            }
//            TextField(
//                value = localVm.selectedProfile?.name ?: "",
//                onValueChange = { localVm.selectedProfile?.name = it },
//                label = { Text("Profile Name") },
//                singleLine = true,
//                modifier =  Modifier.fillMaxWidth()
//            )
        }
    }
}

// **********************************************************************************
// Previews:         (Ignoring large screens for now)
// **********************************************************************************
@Composable
fun EditProfilesScreen_Preview() {
    val navController = rememberNavController()
    val shVm = viewModel<ConnectFourViewModel>()
    shVm.userProfiles.add(UserProfile(
        pName = "exampleUserProfile",
        pAvatarID = AvatarIDs.POO_EMOJI
    ))
    EditProfilesScreen(navController, shVm)
}

@Preview(name = "5-inch Device Portrait",
    widthDp = previewWidthDp, heightDp = previewHeightDp, showBackground = true)
@Composable
fun EditProfilesScreen_Preview5Inch() {
    EditProfilesScreen_Preview()
}
@Preview(name = "5-inch Device Landscape",
    widthDp = previewHeightDp, heightDp = previewWidthDp, showBackground = true)
@Composable
fun EditProfilesScreen_Preview5InchLand() {
    EditProfilesScreen_Preview()
}

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