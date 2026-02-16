package com.example.udmath.presentation.auth.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _state = MutableStateFlow(AdminState())
    val state: StateFlow<AdminState> = _state

    init {
        loadUsers()
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