package com.example.mad_assignmen01_connectfour

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileSelectorGridItem(
    selectedProfile: UserProfile? = null,
    userProfile: UserProfile,
    onClick: () -> Unit = {},
    isSelected: Boolean = (selectedProfile == userProfile)
) {
    GridIconButton(
        onClick = onClick,
        color = if(isSelected) Color.Cyan else Color.LightGray,
        imageID = userProfile.avatarID,
        text = userProfile.name
    )
}

@Preview(showBackground = true)
@Composable
fun preview() {
    val sharedViewModel = viewModel<ConnectFourViewModel>()
    ProfileSelectorGridItem(userProfile = sharedViewModel.player1Profile)
}

@Composable
fun GridIconButton(
    color: Color = Color.LightGray,
    imageID: Int,
    onClick: () -> Unit = {},
    text: String = "NO TEXT"
) {
    Column(Modifier.padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        FilledIconButton(
            onClick = { onClick() },
            modifier = Modifier.size(80.dp),
            colors = IconButtonDefaults.filledIconButtonColors(color)
        ) {
            Image(
                painter = painterResource(id = imageID),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            )
        }
        Text(text)
    }
}

@Composable
fun ProfileSelector(shVm: ConnectFourViewModel, localVm: EditProfilesViewModel,
                    onAddButtonClicked: () -> Unit, modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(if (localVm.selectedProfile == null) "Please Select a Profile:"
            else "Selected Profile: ${localVm.selectedProfile?.name}", fontSize = 20.sp)
        Column(Modifier.border(2.dp, MaterialTheme.colorScheme.secondary)
            .padding(10.dp)
        ) {
            Text("Default Profiles:")
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 90.dp)
            ) {
                items(listOf(
                    shVm.computerProfile,
                    shVm.player1Profile,
                    shVm.player2Profile)
                ) { profile ->
                    ProfileSelectorGridItem(localVm.selectedProfile, profile,
                        onClick = { localVm.selectedProfile =
                            if (localVm.selectedProfile == profile) null else profile }
                    )
                }
            }

            Text("User Profiles:")
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 90.dp)
            ) {
                items(shVm.userProfiles) {profile ->
                    ProfileSelectorGridItem(localVm.selectedProfile, profile,
                        onClick = onAddButtonClicked)
                }
                item { GridIconButton(color = Color.Gray, imageID = R.drawable.add_symbol) }
            }
        }
    }
}