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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NivelProgressUi(
    val respondidas: Int = 0,
    val total: Int = 0,
    val correctas: Int = 0,
    val porcentajeCorrecto: Float = 0f,
    val completadoCorrectamente: Boolean = false
)

data class MateriasUiState(
    val loadingNiveles: Boolean = false,
    val niveles: List<Materia> = emptyList(),
    val selectedNivelId: String? = null,
    val loadingPreguntas: Boolean = false,
    val preguntas: List<Pregunta> = emptyList(),
    val respuestas: Map<String, String> = emptyMap(),
    val correctas: Set<String> = emptySet(),
    val incorrectas: Set<String> = emptySet(),
    val puntaje: Int = 0,
    val error: String? = null,
    val dragStates: Map<String, DragBlankUiState> = emptyMap(),
    val progresoPorNivel: Map<String, NivelProgressUi> = emptyMap(),
    val totalPreguntasMateria: Int = 0,
    val totalCorrectasMateria: Int = 0,
    val totalRespondidasMateria: Int = 0
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
            _state.value = _state.value.copy(
                loadingNiveles = true,
                error = null,
                progresoPorNivel = emptyMap(),
                totalPreguntasMateria = 0,
                totalCorrectasMateria = 0,
                totalRespondidasMateria = 0
            )

            runCatching { repo.getTematicasByMateriaId(materiaId) }
                .onSuccess { niveles ->
                    _state.value = _state.value.copy(
                        loadingNiveles = false,
                        niveles = niveles
                    )
                    loadResumenProgresoMateria(materiaId, niveles)
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(loadingNiveles = false, error = e.message)
                }
        }
    }

    private fun esRespuestaCorrecta(pregunta: Pregunta, respuesta: String): Boolean {
        val tipo = pregunta.tipo.trim().lowercase()
        return if (tipo in TIPOS_MINIJUEGO) {
            respuesta == MINI_JUEGO_WIN || respuesta == "correcto"
        } else {
            respuesta == pregunta.respuestaCorrecta
        }
    }

    private fun loadResumenProgresoMateria(materiaId: String, niveles: List<Materia>) {
        val uid = currentUid ?: return

        viewModelScope.launch {
            val progresoPairs = niveles.map { nivel ->
                async {
                    val preguntas = runCatching {
                        repo.getPreguntasByNivel(materiaId, nivel.id)
                    }.getOrDefault(emptyList())

                    val progreso = runCatching {
                        repo.getProgreso(uid, materiaId, nivel.id)
                    }.getOrNull()

                    val respuestas = progreso?.respuestas.orEmpty()
                    val correctas = preguntas.count { pregunta ->
                        val respuesta = respuestas[pregunta.id] ?: return@count false
                        esRespuestaCorrecta(pregunta, respuesta)
                    }
                    val total = if (preguntas.isNotEmpty()) preguntas.size else nivel.totalpreguntas
                    val respondidas = respuestas.size.coerceAtMost(total)
                    val porcentaje = if (total == 0) 0f else correctas.toFloat() / total.toFloat()

                    nivel.id to NivelProgressUi(
                        respondidas = respondidas,
                        total = total,
                        correctas = correctas,
                        porcentajeCorrecto = porcentaje,
                        completadoCorrectamente = total > 0 && correctas == total
                    )
                }
            }.awaitAll().toMap()

            _state.value = _state.value.copy(
                progresoPorNivel = progresoPairs,
                totalPreguntasMateria = progresoPairs.values.sumOf { it.total },
                totalCorrectasMateria = progresoPairs.values.sumOf { it.correctas },
                totalRespondidasMateria = progresoPairs.values.sumOf { it.respondidas }
            )
        }
    }

    fun registrarMinijuegoResultado(preguntaId: String, gano: Boolean) {
        val p = _state.value.preguntas.firstOrNull { it.id == preguntaId } ?: return
        val tipo = p.tipo.trim().lowercase()
        if (tipo !in TIPOS_MINIJUEGO) return

        val wasAnsweredBefore = _state.value.respuestas.containsKey(preguntaId)
        val previousWasCorrect = _state.value.correctas.contains(preguntaId)
        val opcionGuardada = if (gano) MINI_JUEGO_WIN else "__LOSE__"

        val newRespuestas = _state.value.respuestas + (preguntaId to opcionGuardada)
        val newCorrectas = if (gano) _state.value.correctas + preguntaId else _state.value.correctas - preguntaId
        val newIncorrectas = if (!gano) _state.value.incorrectas + preguntaId else _state.value.incorrectas - preguntaId

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
                    val progreso = currentUid?.let { repo.getProgreso(it, materiaId, nivelId) }
                    val respuestas = progreso?.respuestas ?: emptyMap()

                    val correctas = preguntas.filter { p ->
                        val ans = respuestas[p.id] ?: return@filter false
                        esRespuestaCorrecta(p, ans)
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
            setContext(materiaId, nivelId)

            val pregunta = _state.value.preguntas.firstOrNull { it.id == preguntaId }
                ?: runCatching { repo.getPreguntasByNivel(materiaId, nivelId) }
                    .getOrNull()
                    ?.firstOrNull { it.id == preguntaId }
                ?: return@launch

            val tipo = pregunta.tipo.trim().lowercase()
            if (tipo !in TIPOS_MINIJUEGO) return@launch

            val wasAnsweredBefore = _state.value.respuestas.containsKey(preguntaId)
            val previousWasCorrect = _state.value.correctas.contains(preguntaId)
            val prevAns = _state.value.respuestas[preguntaId]
            val alreadyWon = (prevAns == MINI_JUEGO_WIN || prevAns == "correcto")
            val opcionGuardada = if (gano) MINI_JUEGO_WIN else "__LOSE__"

            val newRespuestas = _state.value.respuestas + (preguntaId to opcionGuardada)
            val newCorrectas = if (gano) _state.value.correctas + preguntaId else _state.value.correctas - preguntaId
            val newIncorrectas = if (!gano) _state.value.incorrectas + preguntaId else _state.value.incorrectas - preguntaId

            val delta = when {
                !wasAnsweredBefore && gano -> pregunta.puntos
                wasAnsweredBefore && previousWasCorrect && !gano -> -pregunta.puntos
                wasAnsweredBefore && !previousWasCorrect && gano -> pregunta.puntos
                else -> 0
            }

            val deltaFinal = if (gano && alreadyWon) 0 else delta

            _state.value = _state.value.copy(
                respuestas = newRespuestas,
                correctas = newCorrectas,
                incorrectas = newIncorrectas,
                puntaje = (_state.value.puntaje + deltaFinal).coerceAtLeast(0)
            )

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
