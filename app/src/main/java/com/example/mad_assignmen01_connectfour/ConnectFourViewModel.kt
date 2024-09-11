package com.example.mad_assignmen01_connectfour

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ConnectFourViewModel: ViewModel() {
    /**
     * template for view model variable
     */
    var exampleVar by mutableStateOf("")
        private set
    fun clearExampleVar(): Unit {
        exampleVar = ""
    }

    val computerProfile = UserProfile("AI", AvatarIDs.COMPUTER)
    var player1Profile by mutableStateOf(UserProfile("Player 1", AvatarIDs.BLANK_AVATAR))
    var player2Profile by mutableStateOf(UserProfile("Player 2", AvatarIDs.BLANK_AVATAR))

    var userProfiles = mutableStateListOf<UserProfile>(
//        UserProfile("Player 1", AvatarIDs.BLANK_AVATAR),
//        UserProfile("Player 2",  AvatarIDs.BLANK_AVATAR)
    )
}
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
// **** CREATE LOCALLY SCOPED VIEW MODEL TO PASS TO CHILDREN
//val localViewModel = viewModel<VIEWMODEL_CLASS>()\

data object AvatarIDs {
    val POO_EMOJI = R.drawable.poo_emoji
    val BLANK_AVATAR = R.drawable.blank_avatar
    val COMPUTER = R.drawable.computer_avatar
}

