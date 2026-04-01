package com.example.udmath.presentation.auth.verify_email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerifyEmailViewState())
    val uiState: StateFlow<VerifyEmailViewState> = _uiState

    fun checkIfEmailVerified(onVerified: () -> Unit) {
        val user = authRepository.getCurrentFirebaseUser()

        if (user == null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "No hay sesión activa. Inicia sesión nuevamente."
            )
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    message = null,
                    errorMessage = null
                )

                user.reload().await()

                if (user.isEmailVerified) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = "Correo verificado correctamente"
                    )
                    onVerified()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Aún no has verificado tu correo"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error al comprobar la verificación"
                )
            }
        }
    }

    fun resendVerificationEmail() {
        val user = authRepository.getCurrentFirebaseUser()

        if (user == null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "No hay sesión activa. Inicia sesión nuevamente."
            )
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    message = null,
                    errorMessage = null
                )

                user.sendEmailVerification().await()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Te reenviamos el correo de verificación"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "No se pudo reenviar el correo"
                )
            }
        }
    }

    fun logout(onBackToLogin: () -> Unit) {
        authRepository.logout()
        onBackToLogin()
    }
}