package com.example.udmath.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.UserUi
import com.example.udmath.domain.repository.AuthRepository
import com.example.udmath.presentation.home.HomeUiEvent
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : ViewModel() {

    // --- NUEVO: user para el NavigationDrawer ---
    private val _user = MutableStateFlow<UserUi?>(null)
    val user: StateFlow<UserUi?> = _user

    // --- NUEVO: eventos one-shot (logout) ---
    private val _events = Channel<HomeUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // --- tu estado admin existente ---
    private val _state = MutableStateFlow(AdminState())
    val state: StateFlow<AdminState> = _state

    init {
        // Cargar usuario actual (igual que HomeViewModel)
        viewModelScope.launch {
            authRepository.refreshCurrentUser()
        }
        viewModelScope.launch {
            authRepository.currentUser.collect { u ->
                _user.value = u?.let { UserUi(email = it.email, photoUrl = it.photoUrl) }
            }
        }

        // tu init admin
        loadUsers()
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            authRepository.logout()
            _events.send(HomeUiEvent.LoggedOut)
        }
    }

    fun onSearchChange(value: String) {
        _state.value = _state.value.copy(search = value)
    }

    fun loadUsers() {
        _state.value = _state.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val snap = firestore.collection("usuarios").get().await()
                val list = snap.documents.map { doc ->
                    AdminUserRow(
                        id = doc.id,
                        name = doc.getString("name").orEmpty(),
                        email = doc.getString("email").orEmpty()
                    )
                }

                _state.value = _state.value.copy(
                    isLoading = false,
                    users = list
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error consultando usuarios"
                )
            }
        }
    }
}
