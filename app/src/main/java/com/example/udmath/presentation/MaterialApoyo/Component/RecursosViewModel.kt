package com.example.udmath.presentation.MaterialApoyo.Component

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.Recurso
import com.example.udmath.domain.repository.RecursoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// El estado unico que describe la pantalla
data class RecursosUiState(
    val loading: Boolean = false,
    val recursos: List<Recurso> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class RecursosViewModel @Inject constructor(
    private val repo: RecursoRepository
) : ViewModel() {

    // Estado interno modificable _state
    private val _state = mutableStateOf(RecursosUiState())
    // Estado expuesto solo para lectura
    val state: State<RecursosUiState> = _state

    fun load(tipo: String) {
        // Ejecuta código asincronico
        viewModelScope.launch {
            // Limpiamos el estado anterior y activa el loading
            _state.value = RecursosUiState(loading = true)
            // runCatching es como el try/catch
            runCatching { repo.getRecursosPorTipo(tipo) }
                //caso de exito
                .onSuccess { _state.value = RecursosUiState(recursos = it) }
                //caso de error
                .onFailure { _state.value = RecursosUiState(error = it.message) }
        }
    }
}
