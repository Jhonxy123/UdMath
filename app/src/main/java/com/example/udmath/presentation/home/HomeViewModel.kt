package com.example.udmath.presentation.home

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel: ViewModel() {

    private val _drawerState = MutableStateFlow(false)
    val drawerState: StateFlow<Boolean> = _drawerState

    fun onDrawerOpen(){
        _drawerState.value = true
    }

    fun onDrawerClose(){
        _drawerState.value = false
    }

}