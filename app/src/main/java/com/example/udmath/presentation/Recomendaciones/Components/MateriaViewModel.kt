package com.example.udmath.presentation.Recomendaciones.Components

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.Materia
import com.example.udmath.domain.model.Pregunta
import com.example.udmath.domain.model.Progreso
import com.example.udmath.domain.repository.MateriaRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MateriasUiState(
    val loadingNiveles: Boolean = false,
    val niveles: List<Materia> = emptyList(),
    val selectedNivelId: String? = null,
    val loadingPreguntas: Boolean = false,
    val preguntas: List<Pregunta> = emptyList(),
    val respuestas: Map<String, String> = emptyMap(),   // preguntaId -> opcion elegida
    val correctas: Set<String> = emptySet(),            // ids correctas
    val incorrectas: Set<String> = emptySet(),          // ids incorrectas
    val puntaje: Int = 0,
    val error: String? = null
)


@HiltViewModel
class MateriaViewModel @Inject constructor(
    private val repo: MateriaRepository
) : ViewModel() {

    private val _state = mutableStateOf(MateriasUiState())
    val state: State<MateriasUiState> = _state

    private var currentMateriaId: String? = null
    private var currentUid: String? = null

    init {
        currentUid = FirebaseAuth.getInstance().currentUser?.uid
    }


    fun loadNiveles(materiaId: String) {

        saveCurrentProgreso()
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

        saveCurrentProgreso()

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
                    val progreso = repo.getProgreso(currentUid!!, materiaId, nivelId)

                    val respuestas = progreso?.respuestas ?: emptyMap()

                    val correctas = preguntas
                        .filter { respuestas[it.id] == it.respuestaCorrecta }
                        .map { it.id }
                        .toSet()

                    val incorrectas = respuestas.keys.toSet() - correctas

                    _state.value = _state.value.copy(
                        selectedNivelId = nivelId,
                        preguntas = preguntas,
                        respuestas = respuestas,
                        correctas = correctas,
                        incorrectas = incorrectas,
                        puntaje = progreso?.puntos ?: 0,
                        loadingPreguntas = false
                    )

                }

                .onFailure { e ->
                    _state.value = _state.value.copy(loadingPreguntas = false, error = e.message)
                }
        }
    }


    fun responderPregunta(pregunta: Pregunta, opcion: String) {
        val wasAnsweredBefore = _state.value.respuestas.containsKey(pregunta.id)
        val previousWasCorrect = _state.value.correctas.contains(pregunta.id)

        val isCorrect = opcion == pregunta.respuestaCorrecta

        val newRespuestas = _state.value.respuestas + (pregunta.id to opcion)
        val newCorrectas = if (isCorrect) _state.value.correctas + pregunta.id else _state.value.correctas - pregunta.id
        val newIncorrectas = if (!isCorrect) _state.value.incorrectas + pregunta.id else _state.value.incorrectas - pregunta.id

        // Puntaje: suma solo si pasa de no-correcta a correcta (y resta si pasa de correcta a incorrecta)
        val delta = when {
            !wasAnsweredBefore && isCorrect -> pregunta.puntos
            wasAnsweredBefore && previousWasCorrect && !isCorrect -> -pregunta.puntos
            wasAnsweredBefore && !previousWasCorrect && isCorrect -> pregunta.puntos
            else -> 0
        }

        _state.value = _state.value.copy(
            respuestas = newRespuestas,
            correctas = newCorrectas,
            incorrectas = newIncorrectas,
            puntaje = (_state.value.puntaje + delta).coerceAtLeast(0)
        )
    }

    fun saveCurrentProgreso() {
        val uid = currentUid ?: return
        val materiaId = currentMateriaId ?: return
        val nivelId = _state.value.selectedNivelId ?: return

        val progreso = Progreso(
            puntos = _state.value.puntaje,
            respondidas = _state.value.respuestas.size,
            total = _state.value.preguntas.size,
            respuestas = _state.value.respuestas
        )

        viewModelScope.launch {
            repo.saveProgreso(uid, materiaId, nivelId, progreso)
        }
    }


}
