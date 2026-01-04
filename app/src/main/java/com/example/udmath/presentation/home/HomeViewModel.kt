// presentation/home/HomeViewModel.kt
package com.example.udmath.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.UserUi
import com.example.udmath.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Eventos one-shot que la UI puede observar
sealed class HomeUiEvent {
    object LoggedOut : HomeUiEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<UserUi?>(null)
    val user: StateFlow<UserUi?> = _user


    init {
        viewModelScope.launch {
            authRepository.refreshCurrentUser()
        }
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                _user.value = user?.let { UserUi(email = it.email, photoUrl = it.photoUrl) }
            }
        }
    }



    // -------- tu estado existente --------
    private val _drawerState = MutableStateFlow(false)
    val drawerState: StateFlow<Boolean> = _drawerState

    private val _currentSection = MutableStateFlow("Inicio")
    val currentSection: StateFlow<String> = _currentSection

    private val _selectedBottomItem = MutableStateFlow(0)
    val selectedBottomItem: StateFlow<Int> = _selectedBottomItem

    // -------- NUEVO: eventos de UI --------
    private val _events = Channel<HomeUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

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


    // -------- NUEVO: cerrar sesión --------
    fun onLogoutClicked() {
        viewModelScope.launch {
            // 1. cerrar sesión en Firebase a través del repositorio
            authRepository.logout()

            // 2. avisar a la UI que ya se cerró sesión
            _events.send(HomeUiEvent.LoggedOut)
        }
    }
}
