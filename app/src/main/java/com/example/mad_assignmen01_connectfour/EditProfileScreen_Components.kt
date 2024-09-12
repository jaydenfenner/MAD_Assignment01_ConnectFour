package com.example.mad_assignmen01_connectfour

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.compose.rememberNavController

@Composable
fun AvatarSelector(localVm: EditProfilesViewModel,
                   onClick: (avatarID: Int) -> Unit
) {
    Column(Modifier.fillMaxWidth().heightIn(max = 400.dp)) {
        Text("Choose Avatar:", fontSize = 20.sp)
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
            columns = GridCells.Adaptive(minSize = 60.dp)
        ) {
            items(UserAvatarIDsList) { avatarID ->
                Image(
                    painter = painterResource(id = avatarID),
                    contentDescription = "",
                    modifier = Modifier
                        .background(
                            color = if (localVm.selectedProfile?.avatarID == avatarID) Color.Cyan
                            else Color.Transparent,
                            shape = CircleShape
                        )
                        .size(50.dp)
                        .clickable { onClick(avatarID) }
                )
            }
        }
    }
}

@Composable
fun ProfileSelectorGridItem(
    thisItemProfile: UserProfile,
    selectedProfile: UserProfile? = null,
    unavailableProfile: UserProfile? = null,
    onClick: () -> Unit = {},
    isUnavailable: Boolean = (unavailableProfile == thisItemProfile),
    isSelected: Boolean = (selectedProfile == thisItemProfile),
) {
    GridIconButton(
        onClick = onClick,
        enabledColor = if(isSelected) Color.Cyan else Color.LightGray, // note disabled is darker
        imageID = thisItemProfile.avatarID,
        text = thisItemProfile.name,
        enabled = !isUnavailable,
    )
}

@Preview(showBackground = true)
@Composable
fun preview() {
    val sharedViewModel = viewModel<ConnectFourViewModel>()
    ProfileSelectorGridItem(thisItemProfile = sharedViewModel.player1Profile)
}

@Preview(showBackground = true)
@Composable
fun preview_disabled() {
    val sharedViewModel = viewModel<ConnectFourViewModel>()
    ProfileSelectorGridItem(
        thisItemProfile = sharedViewModel.player1Profile,
        isUnavailable = true)
}

@Composable
fun GridIconButton(
    enabledColor: Color = Color.LightGray,
    imageID: Int,
    onClick: () -> Unit = {},
    text: String = "NO TEXT",
    enabled: Boolean = true,
) {
    Column(Modifier.padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        FilledIconButton(
            enabled = enabled,
            onClick = { onClick() },
            modifier = Modifier.size(80.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = enabledColor, disabledContainerColor = Color.DarkGray)
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

@Preview(showBackground = true)
@Composable
fun PreviewProfileSelector() {
    val navController = rememberNavController()
    val shVm = viewModel<ConnectFourViewModel>()
    val localVm = viewModel<EditProfilesViewModel>()
    shVm.userProfiles.add(UserProfile(
        pName = "some stupidly long user name which is way too long to fit",
        pAvatarID = AvatarIDs.POO_EMOJI
    ))
    ProfileSelector(shVm = shVm, localVm = localVm,
        onProfileSelected = { profile ->
            localVm.selectedProfile =
                if (localVm.selectedProfile == profile) null
                else profile
        },
        onAddButtonClicked = {},
    )
}

@Composable
fun ProfileSelector(shVm: ConnectFourViewModel, localVm: EditProfilesViewModel,
                    onProfileSelected: (profile: UserProfile) -> Unit,
                    onAddButtonClicked: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)) {
        Text("Please select a profile to edit:", fontSize = 20.sp)
        Column(
            Modifier
                .wrapContentSize()
                .border(2.dp, MaterialTheme.colorScheme.secondary)
                .padding(10.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 90.dp)
            ) {
                item(span = {GridItemSpan(maxLineSpan)}) {Text("Default Profiles:")}
                items(listOf(
                    shVm.computerProfile,
                    shVm.player1Profile,
                    shVm.player2Profile)
                ) { profile ->
                    ProfileSelectorGridItem(
                        thisItemProfile = profile,
                        selectedProfile = localVm.selectedProfile,
                        onClick = { onProfileSelected(profile) }
                    )
                }
                item(span = {GridItemSpan(maxLineSpan)}) {Text("User Profiles:")}
                items(shVm.userProfiles) {profile ->
                    ProfileSelectorGridItem(
                        thisItemProfile = profile,
                        selectedProfile = localVm.selectedProfile,
                        onClick = { onProfileSelected(profile) })
                }
                item {
                    GridIconButton(enabledColor = Color.Gray, imageID = R.drawable.add_symbol,
                        onClick = onAddButtonClicked)
                }
            }
        }
    }
}