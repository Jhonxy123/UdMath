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


    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword)
    }


    fun togglePasswordVisibility(){
        _uiState.value = _uiState.value.copy(
            showPassword = !_uiState.value.showPassword
        )
    }

    fun toggleConfirmPasswordVisibility(){
        _uiState.value = _uiState.value.copy(
            showConfirmPassword = !_uiState.value.showConfirmPassword
        )
    }


    // 🔹 acción de registrar
    fun register(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.isLoading) return

        // Validaciones
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Correo y contraseña son obligatorios")
            return
        } else if (state.password != state.confirmPassword) {
            _uiState.value = state.copy(errorMessage = "Las contraseñas no coinciden")
            return
        } else if (state.code.isBlank()) {
            _uiState.value = state.copy(errorMessage = "El código es obligatorio")
            return
        } else if (!state.code.all { it.isDigit() }) {
            _uiState.value = state.copy(errorMessage = "El código solo debe contener números")
            return
        }


        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

                // 1️⃣ Crear usuario en Firebase Auth
                val authResult = authRepository.registerUser(
                    state.email,
                    state.password
                )

                if (authResult == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo crear el usuario"
                    )
                    return@launch
                }

                val uid = authResult.user?.uid
                if (uid.isNullOrBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "UID inválido"
                    )
                    return@launch
                }

                // 2️⃣ Crear objeto User con UID real
                val user = User(
                    id = uid,
                    code = state.code,
                    name = state.name,
                    email = state.email,
                    photoUrl = null
                )

                // 3️⃣ Guardar en Firestore
                val saved = authRepository.registerUserInFirestore(user)

                if (saved) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No se pudo guardar el perfil"
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
