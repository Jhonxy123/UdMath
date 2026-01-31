package com.example.udmath.presentation.MaterialApoyo.Component

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.Recurso
import com.example.udmath.domain.repository.RecursoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecursosUiState(
    val loading: Boolean = false,
    val recursos: List<Recurso> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class RecursosViewModel @Inject constructor(
    private val repo: RecursoRepository
) : ViewModel() {

    private val _state = mutableStateOf(RecursosUiState())
    val state: State<RecursosUiState> = _state

    fun load(tipo: String) {
        viewModelScope.launch {
            _state.value = RecursosUiState(loading = true)
            runCatching { repo.getRecursosPorTipo(tipo) }
                .onSuccess { _state.value = RecursosUiState(recursos = it) }
                .onFailure { _state.value = RecursosUiState(error = it.message) }
        }
    }
}
