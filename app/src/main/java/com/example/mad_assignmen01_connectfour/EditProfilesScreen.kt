package com.example.mad_assignmen01_connectfour

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class EditProfilesViewModel: ViewModel() {
    var selectedProfile by mutableStateOf<UserProfile?>(null)
}

@Composable
fun EditProfilesScreen(shVm: ConnectFourViewModel) {
    val localVm = viewModel<EditProfilesViewModel>()
    val orientation = LocalConfiguration.current.orientation

//    TODO remove
//    shVm.userProfiles.add(UserProfile("test", AvatarIDs.POO_EMOJI))
//    localVm.selectedProfile = shVm.userProfiles[0]

    InsetContent {
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT ->
                EditProfiles_Portrait(shVm = shVm, localVm)
            else ->
                EditProfiles_Landscape(shVm = shVm, localVm)
        }
    }
}

// TODO choose colour // MOVE TO GAME SCREEN

@Composable
fun EditProfiles_Portrait(shVm: ConnectFourViewModel, localVm: EditProfilesViewModel
) {
    val focusManager = LocalFocusManager.current
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        ProfileSelector(
            shVm = shVm, localVm = localVm,
            onProfileSelected = { profile ->
                localVm.selectedProfile =
                    if (localVm.selectedProfile == profile) null
                    else profile
                focusManager.clearFocus()
            },
            onAddButtonClicked = {
                val newProfile = UserProfile("New Profile", AvatarIDs.POO_EMOJI)
                shVm.userProfiles.add(newProfile)
                localVm.selectedProfile = newProfile
                focusManager.clearFocus()
            },
        )
        Spacer(Modifier.size(10.dp))
        Text("Customize selected profile:", fontSize = 20.sp)
        if (localVm.selectedProfile != null) {
            TextField(
                singleLine = true,
                value = localVm.selectedProfile?.name ?: "",
                onValueChange = { localVm.selectedProfile?.name = it },
                label = { Text("Profile Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (localVm.selectedProfile in shVm.userProfiles) {
            AvatarSelector(localVm,
                onClick = { avatarID ->
                    localVm.selectedProfile?.avatarID = avatarID // does nothing if no selection
                }
            )
            Button(onClick = {
                shVm.userProfiles.remove(localVm.selectedProfile)
                localVm.selectedProfile = null
            }) {
                Text(text = "Delete Profile")
            }
        }
    }
}

@Composable
fun EditProfiles_Landscape(shVm: ConnectFourViewModel, localVm: EditProfilesViewModel) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .weight(1f)
        ) {
            ProfileSelector(
                shVm = shVm, localVm = localVm,
                onProfileSelected = {profile ->
                    localVm.selectedProfile =
                        if (localVm.selectedProfile == profile) null
                        else profile
                    focusManager.clearFocus()
                },
                onAddButtonClicked = {
                    val newProfile = UserProfile("New Profile", AvatarIDs.POO_EMOJI)
                    shVm.userProfiles.add(newProfile)
                    localVm.selectedProfile = newProfile
                    focusManager.clearFocus()
                }
            )
        }
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .weight(1f)
        ) {
            Text("Customize selected profile:", fontSize = 20.sp)
            if (localVm.selectedProfile != null) {
                TextField(
                    singleLine = true,
                    value = localVm.selectedProfile?.name ?: "",
                    onValueChange = { localVm.selectedProfile?.name = it },
                    label = { Text("Profile Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (localVm.selectedProfile in shVm.userProfiles) {
                AvatarSelector(localVm,
                    onClick = {avatarID ->
                        localVm.selectedProfile?.avatarID = avatarID // does nothing if no selection
                    }
                )
                Button(onClick = {
                    shVm.userProfiles.remove(localVm.selectedProfile)
                    localVm.selectedProfile = null
                }) {
                    Text(text = "Delete Profile")
                }
            }
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
    EditProfilesScreen(shVm)
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