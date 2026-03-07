package com.example.udmath.presentation.admin.Contenido


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.Recurso
import com.example.udmath.domain.repository.RecursoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminEditarRecursoUiState(
    val loading: Boolean = false,
    val saving: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val recurso: Recurso? = null
)

@HiltViewModel
class AdminEditarRecursoViewModel @Inject constructor(
    private val repo: RecursoRepository
) : ViewModel() {

    private val _state = mutableStateOf(AdminEditarRecursoUiState())
    val state: State<AdminEditarRecursoUiState> = _state

    fun loadRecurso(recursoId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                loading = true,
                error = null,
                success = false
            )

            runCatching { repo.getRecursoById(recursoId) }
                .onSuccess { recurso ->
                    _state.value = _state.value.copy(
                        loading = false,
                        recurso = recurso,
                        error = if (recurso == null) "No se encontró el recurso" else null
                    )
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(
                        loading = false,
                        error = e.message
                    )
                }
        }
    }

    fun guardarCambios(recurso: Recurso) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                saving = true,
                error = null,
                success = false
            )

            runCatching { repo.editarRecurso(recurso) }
                .onSuccess {
                    _state.value = _state.value.copy(
                        saving = false,
                        success = true,
                        recurso = recurso
                    )
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(
                        saving = false,
                        error = e.message
                    )
                }
        }
    }
}