package com.example.mad_assignmen01_connectfour

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Preview(showBackground = true)
@Composable
fun StartGameScreenButtons(
    modifier: Modifier = Modifier,
    onStartStandard: () -> Unit = {},
    onStartSmall: () -> Unit = {},
    onStartLarge: () -> Unit = {},
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = onStartStandard, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Start Standard Game (7x6)")
        }
        Button(onClick = onStartSmall, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Start Small Game (6x5)")
        }
        Button(onClick = onStartLarge, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Start Large Game (8x7)")
        }
    }
}

@Composable
fun GamePlayerSelector(shVm: ConnectFourViewModel,
                       prompt: String,
                       selectedProfile: UserProfile,
                       defaultProfile: UserProfile,
                       unavailableProfile: UserProfile? = null,
                       onProfileSelected: (profile: UserProfile) -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .heightIn(max = 400.dp)) {
        Text(prompt, fontSize = 20.sp)
        Column(
            Modifier
                .wrapContentSize()
                .border(2.dp, MaterialTheme.colorScheme.secondary)
                .padding(10.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 90.dp)
            ) {
                item(span = {GridItemSpan(maxLineSpan)}) { Text("Default Profiles:") }
                item { ProfileSelectorGridItem(
                    thisItemProfile = defaultProfile,
                    selectedProfile = selectedProfile,
                    unavailableProfile = unavailableProfile,
                    onClick = { onProfileSelected(defaultProfile) }
                    )
                }
                item(span = {GridItemSpan(maxLineSpan)}) { Text("User Profiles:") }
                if (shVm.userProfiles.isEmpty()) {
                    item(span = {GridItemSpan(maxLineSpan)}) {
                        Text(text = "(There are no user profiles configured)")
                    }
                }
                items(shVm.userProfiles) {profile ->
                    ProfileSelectorGridItem(
                        thisItemProfile = profile,
                        selectedProfile = selectedProfile,
                        unavailableProfile = unavailableProfile,
                        onClick = { onProfileSelected(profile) })
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_GamePlayerSelector() {
    val shVm = viewModel<ConnectFourViewModel>()

    var selectedProfile by remember { mutableStateOf(shVm.player1Profile)}
    GamePlayerSelector(shVm = shVm, 
        selectedProfile = selectedProfile, defaultProfile = shVm.player1Profile,
        onProfileSelected = {selectedProfile = it},
        prompt = "Please select a profile to use"
    )
}