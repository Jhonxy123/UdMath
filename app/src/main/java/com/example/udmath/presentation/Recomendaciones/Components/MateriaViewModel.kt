package com.example.udmath.presentation.Recomendaciones.Components

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.Materia
import com.example.udmath.domain.model.Pregunta
import com.example.udmath.domain.repository.MateriaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MateriasUiState(
    val loadingNiveles: Boolean = false,
    val niveles: List<Materia> = emptyList(),
    val selectedNivelId: String? = null,
    val loadingPreguntas: Boolean = false,
    val preguntas: List<Pregunta> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class MateriaViewModel @Inject constructor(
    private val repo: MateriaRepository
) : ViewModel() {

    private val _state = mutableStateOf(MateriasUiState())
    val state: State<MateriasUiState> = _state

    private var currentMateriaId: String? = null

    fun loadNiveles(materiaId: String) {
        currentMateriaId = materiaId
        viewModelScope.launch {
            _state.value = _state.value.copy(loadingNiveles = true, error = null)

            runCatching { repo.getTematicasByMateriaId(materiaId) }
                .onSuccess { niveles ->
                    _state.value = _state.value.copy(loadingNiveles = false, niveles = niveles)
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(loadingNiveles = false, error = e.message)
                }
        }
    }

    fun loadPreguntas(nivelId: String) {
        val materiaId = currentMateriaId ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                selectedNivelId = nivelId,
                loadingPreguntas = true,
                preguntas = emptyList(),
                error = null
            )

            runCatching { repo.getPreguntasByNivel(materiaId, nivelId) }
                .onSuccess { preguntas ->
                    _state.value = _state.value.copy(loadingPreguntas = false, preguntas = preguntas)
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(loadingPreguntas = false, error = e.message)
                }
        }
    }
}
