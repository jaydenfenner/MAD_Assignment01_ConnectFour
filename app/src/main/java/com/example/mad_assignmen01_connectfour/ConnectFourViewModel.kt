package com.example.mad_assignmen01_connectfour

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class ConnectFourViewModel: ViewModel() {
    /**
     * template for view model variable
     */
    var exampleVar by mutableStateOf("")
        private set
    fun clearExampleVar(): Unit {
        exampleVar = ""
    }
}

// **** ACCESS VIEW MODEL SCOPED TO THE ACTIVITY FROM ANY COMPOSABLE:
//val activityViewModel = viewModel<ConnectFourViewModel>(
//    viewModelStoreOwner = LocalContext.current as ComponentActivity
//)

// **** CREATE LOCALLY SCOPED VIEW MODEL TO PASS TO CHILDREN
//val localViewModel = viewModel<VIEWMODEL_CLASS>()

// **** NOTE YOU CAN PASS ANY VIEW MODEL AS AN ARGUMENT