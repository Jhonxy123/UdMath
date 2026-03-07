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

data class AdminRecursosUiState(
    val loading: Boolean = false,
    val recursos: List<Recurso> = emptyList(),
    val error: String? = null,
    val saving: Boolean = false,
    val deleting: Boolean = false,
    val successMessage: String? = null
)

@HiltViewModel
class AdminRecursosViewModel @Inject constructor(
    private val repo: RecursoRepository
) : ViewModel() {

    private val _state = mutableStateOf(AdminRecursosUiState())
    val state: State<AdminRecursosUiState> = _state

    fun loadByModulo(modulo: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                loading = true,
                error = null,
                successMessage = null
            )

            runCatching { repo.getRecursosPorModulo(modulo) }
                .onSuccess { recursos ->
                    _state.value = _state.value.copy(
                        loading = false,
                        recursos = recursos
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

    fun eliminarRecurso(id: String, modulo: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                deleting = true,
                error = null,
                successMessage = null
            )

            runCatching { repo.eliminarRecurso(id) }
                .onSuccess {
                    _state.value = _state.value.copy(
                        deleting = false,
                        successMessage = "Recurso eliminado"
                    )
                    loadByModulo(modulo)
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(
                        deleting = false,
                        error = e.message
                    )
                }
        }
    }

    fun editarRecurso(recurso: Recurso, modulo: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                saving = true,
                error = null,
                successMessage = null
            )

            runCatching { repo.editarRecurso(recurso) }
                .onSuccess {
                    _state.value = _state.value.copy(
                        saving = false,
                        successMessage = "Recurso actualizado"
                    )
                    loadByModulo(modulo)
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(
                        saving = false,
                        error = e.message
                    )
                }
        }
    }

    fun clearMessage() {
        _state.value = _state.value.copy(successMessage = null)
    }
}