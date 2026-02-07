package com.example.udmath.presentation.Recomendaciones.Algebra

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class NivelUi(
    val id: String,
    val numero: Int,
    val titulo: String,
    val totalPreguntas: Int,
    val expanded: Boolean = false,
    val isLoadingPreguntas: Boolean = false,
    val preguntas: List<Pregunta> = emptyList()
)

class AlgebraViewModel(
    private val getNiveles: GetNivelesUseCase,
    private val getPreguntas: GetPreguntasDeNivelUseCase
) : ViewModel() {

    private val _niveles = MutableStateFlow<List<NivelUi>>(emptyList())
    val niveles = _niveles.asStateFlow()

    fun loadAlgebra() = viewModelScope.launch {
        val nivelesDomain = getNiveles("algebra")
        // OJO: aquí necesitas el id del doc del nivel -> doc.id (nivelId)
        // Tu Materia actual no tiene id. Te recomiendo agregar id a Materia.
    }

    fun toggleNivel(materiaId: String, nivelId: String) = viewModelScope.launch {
        val current = _niveles.value

        val idx = current.indexOfFirst { it.id == nivelId }
        if (idx == -1) return@launch

        val item = current[idx]
        val willExpand = !item.expanded

        // 1) Solo cambia expanded
        _niveles.value = current.toMutableList().also { list ->
            list[idx] = item.copy(expanded = willExpand)
        }

        // 2) Si se expande y no tiene preguntas, cargarlas
        if (willExpand && item.preguntas.isEmpty()) {
            _niveles.value = _niveles.value.toMutableList().also { list ->
                val it2 = list[idx]
                list[idx] = it2.copy(isLoadingPreguntas = true)
            }

            val preguntas = getPreguntas(materiaId, nivelId)

            _niveles.value = _niveles.value.toMutableList().also { list ->
                val it2 = list[idx]
                list[idx] = it2.copy(
                    isLoadingPreguntas = false,
                    preguntas = preguntas
                )
            }
        }
    }
}
