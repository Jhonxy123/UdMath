package com.example.udmath.presentation.Recomendaciones.Components.PreguntaDAD

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PreguntaDragScreen(
    preguntaTexto: String,
    opciones: List<String>,
    respuestaCorrecta: String,
    vm: PreguntaDragViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val ui = vm.ui.value

    DragFillBlankQuestion(
        statement = preguntaTexto,
        options = opciones,
        correctAnswer = respuestaCorrecta,
        ui = ui,
        onDropRectChanged = vm::onDropRectChanged,
        onAnswerDropped = vm::onAnswerDropped,
        onClear = vm::onClear
    )
}