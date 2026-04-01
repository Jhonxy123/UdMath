package com.example.udmath.presentation.auth.login

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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginViewState())
    val state: StateFlow<LoginViewState> = _state

    fun onEmailChanged(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun onPasswordChanged(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun openResetDialog() {
        _state.value = _state.value.copy(
            showResetDialog = true,
            resetEmail = _state.value.email,
            resetSuccessMessage = null,
            errorMessage = null
        )
    }

    fun closeResetDialog() {
        _state.value = _state.value.copy(showResetDialog = false)
    }

    fun onResetEmailChanged(email: String) {
        _state.value = _state.value.copy(resetEmail = email)
    }

    fun clearResetSuccessMessage() {
        _state.value = _state.value.copy(resetSuccessMessage = null)
    }

    fun sendPasswordReset() {
        val email = _state.value.resetEmail.trim()

        if (email.isBlank()) {
            _state.value = _state.value.copy(
                errorMessage = "Ingresa tu correo para restablecer la contraseña"
            )
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.value = _state.value.copy(
                errorMessage = "Correo electrónico inválido"
            )
            return
        }

        _state.value = _state.value.copy(
            resetLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                authRepository.sendPasswordReset(email)

                _state.value = _state.value.copy(
                    resetLoading = false,
                    showResetDialog = false,
                    resetSuccessMessage = "Te enviamos un correo para restablecer tu contraseña."
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    resetLoading = false,
                    errorMessage = e.localizedMessage
                        ?: "No se pudo enviar el correo de restablecimiento"
                )
            }
        }
    }

    fun login() {
        val email = _state.value.email.trim()
        val password = _state.value.password

        if (email.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(
                errorMessage = "Por favor llena todos los campos"
            )
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.value = _state.value.copy(
                errorMessage = "Correo electrónico inválido"
            )
            return
        }

        _state.value = _state.value.copy(
            isLoading = true,
            errorMessage = null,
            loginDestination = null
        )

        viewModelScope.launch {
            try {
                val authResult = authRepository.loginWithEmail(email, password)

                val firebaseUser = authResult.user
                    ?: throw IllegalStateException("Usuario inválido")

                firebaseUser.reload().await()

                if (!firebaseUser.isEmailVerified) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        loginDestination = LoginDestination.VERIFY_EMAIL(
                            firebaseUser.email ?: email
                        )
                    )
                    return@launch
                }

                val uid = firebaseUser.uid
                val role = authRepository.getUserRole(uid) ?: "normal"

                val destination =
                    if (role.equals("admin", ignoreCase = true)) {
                        LoginDestination.ADMIN
                    } else {
                        LoginDestination.MAIN
                    }

                _state.value = _state.value.copy(
                    isLoading = false,
                    loginDestination = destination
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "Correo o contraseña incorrectos"
                )
            }
        }
    }

    fun consumeDestination() {
        _state.value = _state.value.copy(loginDestination = null)
    }
}