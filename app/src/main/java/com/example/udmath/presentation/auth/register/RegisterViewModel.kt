// presentation/auth/register/RegisterViewModel.kt
package com.example.udmath.presentation.auth.register

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.User
import com.example.udmath.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterViewState())
    val uiState: StateFlow<RegisterViewState> = _uiState

    // 🔹 actualiza el nombre a medida que el usuario escribe
    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    // actualiza el código a medida que el usuario escribe
    fun onCodeChange(code: String) {
        _uiState.value = _uiState.value.copy(code = code)
    }

    // 🔹 actualiza el email
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    // 🔹 actualiza la contraseña
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }


    // 🔹 acción de registrar
    fun register(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.isLoading) return

        // Validaciones básicas (puedes mejorarlas luego)
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Correo y contraseña son obligatorios")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = state.copy(isLoading = true, errorMessage = null)

                // 1. Crear usuario en Firebase Auth
                val authResult = authRepository.registerUser(state.email, state.password)

                // 2. Guardar datos en Firestore (si quieres)
                val firebaseUser = authResult?.user
                val user = User(
                    id = firebaseUser?.uid ?: "",
                    code = state.code,
                    name = state.name,
                    email = state.email,
                    password = state.password  // o "" si no quieres guardarla
                )

                val saved = authRepository.registerUserInFirestore(user)

                if (authResult != null && saved) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo registrar el usuario"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error desconocido"
                )
            }
        }
    }
}
