// presentation/profile/ProfileViewModel.kt
package com.example.udmath.presentation.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilViewState())
    val uiState: StateFlow<PerfilViewState> = _uiState

    init {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                if (user == null) {
                    _uiState.value = PerfilViewState(
                        isLoading = false,
                        error = "No hay usuario autenticado."
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        email = user.email,
                        photoUrl = user.photoUrl,
                        name = user.name,
                        code = user.code,
                        error = null
                    )
                }
            }
        }
    }

}
