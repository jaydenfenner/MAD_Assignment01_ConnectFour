package com.example.mad_assignmen01_connectfour

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ConnectFourViewModel: ViewModel() {
    var exampleVar by mutableStateOf("")
        private set

    fun clearExampleVar(): Unit {
        exampleVar = ""
    }
}