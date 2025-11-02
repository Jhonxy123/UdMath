// presentation/home/HomeViewModel.kt
package com.example.udmath.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _drawerState = MutableStateFlow(false)
    val drawerState: StateFlow<Boolean> = _drawerState

    private val _currentSection = MutableStateFlow("Inicio")
    val currentSection: StateFlow<String> = _currentSection

    private val _selectedBottomItem = MutableStateFlow(0)
    val selectedBottomItem: StateFlow<Int> = _selectedBottomItem

    fun openDrawer() {
        viewModelScope.launch {
            _drawerState.value = true
        }
    }

    fun closeDrawer() {
        viewModelScope.launch {
            _drawerState.value = false
        }
    }

    fun setCurrentSection(section: String) {
        viewModelScope.launch {
            _currentSection.value = section
            closeDrawer()
        }
    }

    fun setSelectedBottomItem(index: Int) {
        viewModelScope.launch {
            _selectedBottomItem.value = index
        }
    }
}