package com.example.mad_assignmen01_connectfour

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// **** CREATE LOCALLY SCOPED VIEW MODEL TO PASS TO CHILDREN
//val localViewModel = viewModel<VIEWMODEL_CLASS>()\

class ConnectFourViewModel: ViewModel() {
    val computerProfile = UserProfile("AI", AvatarIDs.COMPUTER)
    var player1Profile by mutableStateOf(UserProfile("Player 1", AvatarIDs.BLANK_AVATAR))
    var player2Profile by mutableStateOf(UserProfile("Player 2", AvatarIDs.BLANK_AVATAR))
    var userProfiles = mutableStateListOf<UserProfile>()

    var singlePlayerProfileSelection by mutableStateOf(player1Profile)
    var twoPlayerProfileSelectionP1 by mutableStateOf(player1Profile)
    var twoPlayerProfileSelectionP2 by mutableStateOf(player2Profile)
}

// CLASS DEFINITIONS BELOW
class UserProfile(pName: String, pAvatarID: Int) {
    var name by mutableStateOf("")
    var avatarID by mutableIntStateOf(AvatarIDs.BLANK_AVATAR)
    var numberOfWins by mutableIntStateOf(0)
    var numberOfLosses by mutableIntStateOf(0)
    var numberOfDraws by mutableIntStateOf(0)

    init {
        name = pName
        avatarID = pAvatarID
    }
}

data object AvatarIDs {
    // reserved for default profiles
    val BLANK_AVATAR = R.drawable.blank_avatar
    val COMPUTER = R.drawable.computer_avatar

    // for user
    val POO_EMOJI = R.drawable.poo_emoji
    val CAR = R.drawable.car
    val CAT = R.drawable.cat
    val DOG = R.drawable.dog
    val GHOST = R.drawable.ghost
    val SURFER = R.drawable.man_surfing
    val MONKEY = R.drawable.monkey_face
    val SKULL = R.drawable.skull
    val EVIL_FACE = R.drawable.smiling_imp
    val SUNGLASSES = R.drawable.sunglasses
    val UNICORN = R.drawable.unicorn_face
    val WATER_GUN = R.drawable.water_gun_emoji
}
val UserAvatarIDsList = listOf(
    AvatarIDs.POO_EMOJI,
    AvatarIDs.CAR,
    AvatarIDs.CAT,
    AvatarIDs.DOG,
    AvatarIDs.GHOST,
    AvatarIDs.SURFER,
    AvatarIDs.MONKEY,
    AvatarIDs.SKULL,
    AvatarIDs.EVIL_FACE,
    AvatarIDs.SUNGLASSES,
    AvatarIDs.UNICORN,
    AvatarIDs.WATER_GUN,
)



//class Disc(color: Color, isTaken: Boolean = false)
//var disks = mapOf()<String, Disc>() {
//    "red" to Disc(color = Color.Red)
//    "blue" to Disc(color = Color.Blue)
//    "green" to Disc(color = Color.Green)
//    "yellow" to Disc(color = Color.Yellow)
//    "cyan" to Disc(color = Color.Cyan)
//    "magenta" to Disc(color = Color.Magenta)
//    "purple" to Disc(color = Color.hsv(272.0F, 0.9F, 1.0F))
//}
