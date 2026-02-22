package com.example.udmath.presentation.Recomendaciones.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.udmath.presentation.components.TopBarback

@Composable
fun PreguntasScreen(
    materiaId: String,
    nivelId: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MateriaViewModel = hiltViewModel()
) {
    val ui = viewModel.state.value

    // Carga las preguntas cuando el usuario entra a la pantalla
    // El composaable se puede recomponer mil veces, pero no vuelve a cargar las preguntas si no hay cambios en materiaId o nivelId
    LaunchedEffect(materiaId, nivelId) {
        viewModel.loadNiveles(materiaId)      // para setear currentMateriaId
        viewModel.loadPreguntas(nivelId)      // cargar preguntas
    }

    // Cuando el usuario salga de esta pantalla, guarda el progreso
    // Mientras este en la pantalla no pasará nada
    DisposableEffect(Unit) {
        onDispose { viewModel.saveCurrentProgreso() }
    }


    // Construimos la vista
    Scaffold(
        topBar = { TopBarback("Preguntas", navigateBack = { navigateBack() }) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // calculamos el progreso del usuario
            val respondidas = ui.respuestas.size
            val total = ui.preguntas.size
            val progress = if (total == 0) 0f else respondidas.toFloat() / total.toFloat()

            // mostramos su puntaje
            Text("Puntaje: ${ui.puntaje}", fontWeight = FontWeight.SemiBold)

            // utilizamos un LinearProgressIndicator para mostrar el progreso
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(10.dp)
            )

            Text("$respondidas / $total respondidas")

            Spacer(Modifier.height(12.dp))

            // Dibuja la pantalla de carga si el estado es true
            if (ui.loadingPreguntas) {

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator()
                }
                // @Column Quiere decir que no siga dibujando lo que está debajo de este column (hasta aquí llega la UI por ahora)
                // Sin esto se dibujaría el loader, pero también las preguntas y se vería todo mezclado
                return@Column
            }

            ui.preguntas.forEachIndexed { index, p ->

                when (p.tipo.lowercase()) {

                    "multiple" -> {
                        PreguntaCard(
                            pregunta = p,
                            index = index,
                            selectedOption = ui.respuestas[p.id],
                            isCorrect = ui.correctas.contains(p.id),
                            isIncorrect = ui.incorrectas.contains(p.id),
                            onOpcionSeleccionada = { _, opcion ->
                                viewModel.responderPregunta(p, opcion)
                            }
                        )
                    }

                    "drag" -> {
                        //  reutilizas tu DragFillBlankQuestion
                        DragFillBlankQuestion(
                            statement = p.texto,              // ejemplo "2 + 3 = ____"
                            options = p.opciones,
                            correctAnswer = p.respuestaCorrecta,
                            onAnswered = { selected, isCorrect ->
                                // si quieres guardar respuesta igual que multiple:
                                viewModel.responderPregunta(p, selected)
                            }
                        )
                    }

                    else -> {
                        Text("Tipo no soportado: ${p.tipo}")
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
