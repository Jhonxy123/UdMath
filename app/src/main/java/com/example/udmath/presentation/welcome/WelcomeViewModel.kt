package com.example.udmath.presentation.welcome

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    fun loginWithMicrosoft(activity: Activity, onSuccess: () -> Unit) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = WelcomeUiState(isLoading = true, error = null)

            val result = authRepository.loginWithMicrosoft(activity)

            result.onSuccess {
                _uiState.value = WelcomeUiState(isLoading = false, error = null)
                onSuccess()
            }.onFailure { e ->
                _uiState.value = WelcomeUiState(
                    isLoading = false,
                    error = e.message ?: "Error iniciando con Microsoft"
                )
            }
        }
    }
}
