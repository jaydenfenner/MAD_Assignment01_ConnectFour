package com.example.mad_assignmen01_connectfour

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mad_assignmen01_connectfour.ui.theme.MAD_Assignmen01_ConnectFourTheme

@Composable
fun ChangeDisksScreen(shVm: ConnectFourViewModel) {
    val orientation = LocalConfiguration.current.orientation
    InsetContent {
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT ->
                ChangeDisks_Portrait(shVm = shVm)
            else ->
                ChangeDisks_Landscape(shVm = shVm)
        }
    }
}

@Composable
fun ColorPickerDemo() {
    val colors = listOf(
        Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan, Color.Magenta, Color.Black, Color.Gray
    )
    var selectedColor by remember { mutableStateOf(colors.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(selectedColor)
                .border(2.dp, Color.Black, CircleShape)
        ) {
            // Here is where you can show the selected color
        }
    }
}

@Composable
fun CircleColorItem(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderWidth = if (isSelected) 6.dp else 2.dp
    val borderColor = if (isSelected) Color.Black else Color.Gray

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(color = color)
                .border(borderWidth, borderColor, CircleShape)
                .clickable(onClick = onClick)
        )
    }
}

@Composable
fun DiscColorSelector(
    prompt: String,
    selectedColor: Color,
    unavailableColor: Color,
    onClick: (color: Color) -> Unit,
) {
    val context = LocalContext.current
    Column(
        Modifier
            .border(width = 5.dp, Color.Black)
            .padding(10.dp)
            .fillMaxWidth()
            .heightIn(max = 400.dp)
    ) {
        Text(prompt, fontSize = 20.sp)
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp),
            columns = GridCells.Adaptive(minSize = 60.dp)
        ) {
            items(DiscColorOptions) { color ->
                CircleColorItem(
                    color = color,
                    isSelected = (selectedColor == color),
                    onClick = {
                        if (unavailableColor != color) {
                            onClick(color)
                        } else {
                            Toast.makeText(context, "This color is already taken!",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ChangeDisks_Portrait(shVm: ConnectFourViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        DiscColorSelector(
            prompt = "Disc Colour For Left Side:",
            selectedColor = shVm.leftPlayerDiskColour,
            unavailableColor = shVm.rightPlayerDiskColour,
            onClick = {shVm.leftPlayerDiskColour = it}
        )
        Spacer(modifier = Modifier.height(20.dp))
        DiscColorSelector(
            prompt = "Disc Colour For Right Side:",
            selectedColor = shVm.rightPlayerDiskColour,
            unavailableColor = shVm.leftPlayerDiskColour,
            onClick = {shVm.rightPlayerDiskColour = it}
        )
    }
}

@Composable
fun ChangeDisks_Landscape(shVm: ConnectFourViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row() {
            Column(modifier = Modifier.weight(1f)) {
                DiscColorSelector(
                    prompt = "Disc Colour For Left Side:",
                    selectedColor = shVm.leftPlayerDiskColour,
                    unavailableColor = shVm.rightPlayerDiskColour,
                    onClick = {shVm.leftPlayerDiskColour = it}
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                DiscColorSelector(
                    prompt = "Disc Colour For Right Side:",
                    selectedColor = shVm.rightPlayerDiskColour,
                    unavailableColor = shVm.leftPlayerDiskColour,
                    onClick = {shVm.rightPlayerDiskColour = it}
                )
            }
        }

    }
}

// **********************************************************************************
// Previews:         (Ignoring large screens for now)
// **********************************************************************************
@Composable
fun ChangeDisksScreen_Preview() {
    val shVm = viewModel<ConnectFourViewModel>()
    MAD_Assignmen01_ConnectFourTheme {
        ChangeDisksScreen(shVm = shVm)
    }
}

@Preview(name = "5-inch Device Portrait",
    widthDp = previewWidthDp, heightDp = previewHeightDp, showBackground = true)
@Composable
fun ChangeDisksScreen_Preview5Inch() {
    ChangeDisksScreen_Preview()
}
@Preview(name = "5-inch Device Landscape",
    widthDp = previewHeightDp, heightDp = previewWidthDp, showBackground = true)
@Composable
fun ChangeDisksScreen_Preview5InchLand() {
    ChangeDisksScreen_Preview()
}

// TODO check if we need these, my friend got 100% with no tablet layouts
//@Preview(name = "10-inch Tablet Portrait",
//    widthDp = previewTabletWidthDp, heightDp = previewTabletHeightDp, showBackground = true)
//@Composable
//fun MainMenuScreen_Preview10InchTablet() {
//    MainMenuScreen_Preview()
//}
//@Preview(name = "10-inch Tablet Landscape",
//    widthDp = previewTabletHeightDp, heightDp = previewTabletWidthDp, showBackground = true)
//@Composable
//fun MainMenuScreen_Preview10InchTabletLand() {
//    MainMenuScreen_Preview()
//}