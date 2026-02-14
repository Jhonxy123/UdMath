package com.example.udmath.presentation.Recomendaciones.Components.PreguntaDAD

import androidx.compose.runtime.Composable

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
