package com.example.udmath.presentation.home

import androidx.compose.material3.DrawerValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel: ViewModel() {

    private val _drawerState = MutableStateFlow(DrawerValue.Closed)
    val drawerState = _drawerState




}