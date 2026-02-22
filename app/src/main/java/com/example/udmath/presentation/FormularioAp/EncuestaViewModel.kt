package com.example.udmath.presentation.FormularioAp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.EncuestaResultado
import com.example.udmath.domain.model.ResultadoMateria
import com.example.udmath.domain.repository.StatsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EncuestaUiState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class EncuestaViewModel @Inject constructor(
    private val statsRepo: StatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EncuestaUiState())
    val uiState: StateFlow<EncuestaUiState> = _uiState

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(successMessage = null, errorMessage = null)
    }

    fun enviar(semestre: String, materiaId: String, aprobo: Boolean) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            _uiState.value = _uiState.value.copy(errorMessage = "Debes iniciar sesión.")
            return
        }

        if (_uiState.value.isLoading) return

        val data = EncuestaResultado(
            semestre = semestre,
            materiaId = materiaId,
            resultado = if (aprobo) ResultadoMateria.APROBO else ResultadoMateria.PERDIO
        )

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, successMessage = null, errorMessage = null)

            try {
                statsRepo.submitEncuesta(uid, data)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "✅ Resultado guardado exitosamente"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "❌ No se pudo guardar. Intenta de nuevo."
                )
            }
        }
    }
}