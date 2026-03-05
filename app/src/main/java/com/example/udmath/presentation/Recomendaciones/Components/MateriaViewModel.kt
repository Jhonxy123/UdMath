package com.example.udmath.presentation.Recomendaciones.Components

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.Materia
import com.example.udmath.domain.model.Pregunta
import com.example.udmath.domain.model.Progreso
import com.example.udmath.domain.repository.MateriaRepository
import com.example.udmath.presentation.Recomendaciones.Components.PreguntaDAD.DragBlankUiState
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
    val error: String? = null,
    val dragStates: Map<String, DragBlankUiState> = emptyMap()
)

private const val MINI_JUEGO_WIN = "__WIN__"

private val TIPOS_MINIJUEGO = setOf("tabla_seleccion", "crucigrama_naturales")

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

    fun registrarMinijuegoResultado(preguntaId: String, gano: Boolean) {
        val p = _state.value.preguntas.firstOrNull { it.id == preguntaId } ?: return
        val tipo = p.tipo.trim().lowercase()

        // Solo aplica a minijuegos
        if (tipo !in TIPOS_MINIJUEGO) return

        val wasAnsweredBefore = _state.value.respuestas.containsKey(preguntaId)
        val previousWasCorrect = _state.value.correctas.contains(preguntaId)

        // Si ganó, guardamos un marcador especial "__WIN__"
        // Si perdió, puedes guardar "__LOSE__" o no guardar nada. Yo recomiendo guardar "__LOSE__"
        // para que quede "respondida" y muestre incorrecto.
        val opcionGuardada = if (gano) MINI_JUEGO_WIN else "__LOSE__"

        val newRespuestas = _state.value.respuestas + (preguntaId to opcionGuardada)

        val newCorrectas =
            if (gano) _state.value.correctas + preguntaId else _state.value.correctas - preguntaId

        val newIncorrectas =
            if (!gano) _state.value.incorrectas + preguntaId else _state.value.incorrectas - preguntaId

        // Puntaje: suma solo si pasa a correcta
        val delta = when {
            !wasAnsweredBefore && gano -> p.puntos
            wasAnsweredBefore && previousWasCorrect && !gano -> -p.puntos
            wasAnsweredBefore && !previousWasCorrect && gano -> p.puntos
            else -> 0
        }

        _state.value = _state.value.copy(
            respuestas = newRespuestas,
            correctas = newCorrectas,
            incorrectas = newIncorrectas,
            puntaje = (_state.value.puntaje + delta).coerceAtLeast(0)
        )
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

                    val correctas = preguntas.filter { p ->
                        val ans = respuestas[p.id] ?: return@filter false
                        val tipo = p.tipo.trim().lowercase()

                        val esMiniJuego = tipo in TIPOS_MINIJUEGO
                        if (esMiniJuego) {
                            ans == MINI_JUEGO_WIN || ans == "correcto"
                        } else {
                            ans == p.respuestaCorrecta
                        }
                    }.map { it.id }.toSet()

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

    fun setContext(materiaId: String, nivelId: String) {
        currentMateriaId = materiaId
        _state.value = _state.value.copy(selectedNivelId = nivelId)
    }

    fun registrarMinijuegoResultadoConFetch(
        materiaId: String,
        nivelId: String,
        preguntaId: String,
        gano: Boolean
    ) {
        viewModelScope.launch {
            // asegura contexto para saveCurrentProgreso()
            setContext(materiaId, nivelId)

            // 1) asegurar que tengo la pregunta (para puntos y tipo)
            val pregunta = _state.value.preguntas.firstOrNull { it.id == preguntaId }
                ?: runCatching { repo.getPreguntasByNivel(materiaId, nivelId) }
                    .getOrNull()
                    ?.firstOrNull { it.id == preguntaId }
                ?: return@launch

            val tipo = pregunta.tipo.trim().lowercase()
            if (tipo !in TIPOS_MINIJUEGO) return@launch

            val wasAnsweredBefore = _state.value.respuestas.containsKey(preguntaId)
            val previousWasCorrect = _state.value.correctas.contains(preguntaId)

            // IMPORTANTE: en tu Firestore YA EXISTE "correcto", así que lo tratamos como WIN también
            val prevAns = _state.value.respuestas[preguntaId]
            val alreadyWon = (prevAns == MINI_JUEGO_WIN || prevAns == "correcto")

            val opcionGuardada = if (gano) MINI_JUEGO_WIN else "__LOSE__"

            val newRespuestas = _state.value.respuestas + (preguntaId to opcionGuardada)

            val newCorrectas =
                if (gano) _state.value.correctas + preguntaId else _state.value.correctas - preguntaId

            val newIncorrectas =
                if (!gano) _state.value.incorrectas + preguntaId else _state.value.incorrectas - preguntaId

            val delta = when {
                !wasAnsweredBefore && gano -> pregunta.puntos
                wasAnsweredBefore && previousWasCorrect && !gano -> -pregunta.puntos
                wasAnsweredBefore && !previousWasCorrect && gano -> pregunta.puntos
                else -> 0
            }

            // si ya estaba ganado antes, no sumes puntos otra vez
            val deltaFinal = if (gano && alreadyWon) 0 else delta

            _state.value = _state.value.copy(
                respuestas = newRespuestas,
                correctas = newCorrectas,
                incorrectas = newIncorrectas,
                puntaje = (_state.value.puntaje + deltaFinal).coerceAtLeast(0)
            )

            // 2) guardar
            saveCurrentProgreso()
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

    fun onDragDropRectChanged(preguntaId: String, rect: Rect) {
        val current = _state.value.dragStates.toMutableMap()
        val prev = current[preguntaId] ?: DragBlankUiState()
        current[preguntaId] = prev.copy(dropRect = rect)
        _state.value = _state.value.copy(dragStates = current)
    }

    fun onDragClear(preguntaId: String) {
        val current = _state.value.dragStates.toMutableMap()
        val prev = current[preguntaId] ?: DragBlankUiState()
        if (prev.locked) return
        current[preguntaId] = prev.copy(placedAnswer = null, feedback = null, locked = false)
        _state.value = _state.value.copy(dragStates = current)
    }

    fun onDragAnswerDropped(preguntaId: String, selected: String, correctAnswer: String) {
        val current = _state.value.dragStates.toMutableMap()
        val prev = current[preguntaId] ?: DragBlankUiState()
        if (prev.locked) return

        val isCorrect = selected == correctAnswer
        current[preguntaId] = prev.copy(
            placedAnswer = selected,
            feedback = isCorrect,
            locked = isCorrect
        )
        _state.value = _state.value.copy(dragStates = current)
    }


}
