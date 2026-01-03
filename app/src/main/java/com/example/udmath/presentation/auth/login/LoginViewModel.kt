package com.example.udmath.presentation.auth.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _state = MutableStateFlow(LoginViewState())
    val state: StateFlow<LoginViewState> = _state

    fun onEmailChanged(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun onPasswordChanged(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun login(/*onSuccess: () -> Unit*/) {

        val email = _state.value.email
        val password = _state.value.password

        // 🔒 VALIDACIONES
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

        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                    //onSuccess()
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Correo o contraseña incorrectos"
                    )
                }
            }
    }
}
