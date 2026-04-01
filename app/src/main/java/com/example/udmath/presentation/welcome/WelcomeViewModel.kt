package com.example.udmath.presentation.welcome

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class WelcomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState

    fun setError(msg: String) {
        _uiState.value = _uiState.value.copy(error = msg)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun checkActiveSession(
        onNavigateToHome: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

                val currentUser = authRepository.getCurrentFirebaseUser()

                if (currentUser == null) {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    return@launch
                }

                currentUser.reload().await()

                if (!currentUser.isEmailVerified) {
                    authRepository.logout()
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    return@launch
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
                onNavigateToHome()

            } catch (e: Exception) {
                authRepository.logout()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    fun loginWithMicrosoft(activity: Activity, onSuccess: () -> Unit) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            try {
                _uiState.value = WelcomeUiState(isLoading = true, error = null)

                val result = authRepository.loginWithMicrosoft(activity)

                result.onSuccess {
                    viewModelScope.launch {
                        try {
                            val currentUser = authRepository.getCurrentFirebaseUser()

                            if (currentUser == null) {
                                _uiState.value = WelcomeUiState(
                                    isLoading = false,
                                    error = "No se pudo obtener el usuario autenticado"
                                )
                                return@launch
                            }

                            currentUser.reload().await()

                            if (!currentUser.isEmailVerified) {
                                authRepository.logout()
                                _uiState.value = WelcomeUiState(
                                    isLoading = false,
                                    error = "Debes verificar tu correo antes de ingresar"
                                )
                                return@launch
                            }

                            _uiState.value = WelcomeUiState(isLoading = false, error = null)
                            onSuccess()

                        } catch (e: Exception) {
                            authRepository.logout()
                            _uiState.value = WelcomeUiState(
                                isLoading = false,
                                error = e.message ?: "Error validando la cuenta"
                            )
                        }
                    }
                }.onFailure { e ->
                    _uiState.value = WelcomeUiState(
                        isLoading = false,
                        error = e.message ?: "Error iniciando con Microsoft"
                    )
                }

            } catch (e: Exception) {
                _uiState.value = WelcomeUiState(
                    isLoading = false,
                    error = e.message ?: "Error iniciando con Microsoft"
                )
            }
        }
    }
}