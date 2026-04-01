// presentation/auth/register/RegisterViewModel.kt
package com.example.udmath.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.User
import com.example.udmath.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterViewState())
    val uiState: StateFlow<RegisterViewState> = _uiState

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onCodeChange(code: String) {
        _uiState.value = _uiState.value.copy(code = code)
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            showPassword = !_uiState.value.showPassword
        )
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            showConfirmPassword = !_uiState.value.showConfirmPassword
        )
    }

    fun resetSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }

    fun register() {
        val state = _uiState.value

        if (state.isLoading) return

        if (state.name.isBlank()) {
            _uiState.value = state.copy(errorMessage = "El nombre es obligatorio")
            return
        }

        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Correo y contraseña son obligatorios")
            return
        }

        if (state.password != state.confirmPassword) {
            _uiState.value = state.copy(errorMessage = "Las contraseñas no coinciden")
            return
        }

        if (state.code.isBlank()) {
            _uiState.value = state.copy(errorMessage = "El código es obligatorio")
            return
        }

        if (!state.code.all { it.isDigit() }) {
            _uiState.value = state.copy(errorMessage = "El código solo debe contener números")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

                val authResult = authRepository.registerUser(
                    state.email,
                    state.password
                )

                val firebaseUser = authResult?.user
                val uid = firebaseUser?.uid

                if (firebaseUser == null || uid.isNullOrBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo crear el usuario"
                    )
                    return@launch
                }

                firebaseUser.sendEmailVerification().await()

                val user = User(
                    id = uid,
                    code = state.code,
                    name = state.name,
                    email = state.email,
                    photoUrl = null,
                    role = "normal"
                )

                val saved = authRepository.registerUserInFirestore(user)

                if (!saved) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo guardar el perfil"
                    )
                    return@launch
                }

                authRepository.logout()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    errorMessage = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error desconocido"
                )
            }
        }
    }
}