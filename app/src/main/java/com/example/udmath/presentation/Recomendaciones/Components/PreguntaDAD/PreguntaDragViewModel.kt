package com.example.udmath.presentation.Recomendaciones.Components.PreguntaDAD

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreguntaDragViewModel @Inject constructor() : ViewModel() {

    private val _ui = mutableStateOf(DragBlankUiState())
    val ui: State<DragBlankUiState> = _ui

    fun onDropRectChanged(rect: androidx.compose.ui.geometry.Rect) {
        _ui.value = _ui.value.copy(dropRect = rect)
    }

    fun onClear() {
        if (_ui.value.locked) return
        _ui.value = _ui.value.copy(placedAnswer = null, feedback = null, locked = false)
    }

    fun onAnswerDropped(selected: String, correctAnswer: String) {
        if (_ui.value.locked) return

        val isCorrect = selected == correctAnswer
        _ui.value = _ui.value.copy(
            placedAnswer = selected,
            feedback = isCorrect,
            locked = isCorrect // si quieres bloquear cuando es correcto
        )

        // para guardar progreso/puntos.
    }
}
