package com.example.udmath.presentation.admin.BaseDatos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.UserUi
import com.example.udmath.domain.repository.AdminRepository
import com.example.udmath.domain.repository.AuthRepository
import com.example.udmath.presentation.admin.AdminState
import com.example.udmath.presentation.home.HomeUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BdViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdminState())
    val state: StateFlow<AdminState> = _state

    private val _uiEvents = Channel<BdUiEvent>(Channel.BUFFERED)
    val uiEvents = _uiEvents.receiveAsFlow()

    init { loadUsers() }

    fun loadUsers() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val list = adminRepository.fetchUsers()
                _state.value = _state.value.copy(isLoading = false, users = list)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun requestEdit(userId: String) {
        viewModelScope.launch {
            _uiEvents.send(BdUiEvent.NavigateToEdit(userId))
        }
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            try {
                adminRepository.deleteUser(userId)
                _uiEvents.send(BdUiEvent.ShowMessage("Usuario eliminado"))
                loadUsers()
            } catch (e: Exception) {
                _uiEvents.send(BdUiEvent.ShowMessage("No se pudo eliminar: ${e.message ?: ""}"))
            }
        }
    }

    fun onSearchChange(value: String) {
        _state.value = _state.value.copy(search = value)
    }
}