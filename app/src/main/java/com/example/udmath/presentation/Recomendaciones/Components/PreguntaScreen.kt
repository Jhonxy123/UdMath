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

    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveCurrentProgreso()
        }
    }

    LaunchedEffect(materiaId, nivelId) {
        viewModel.loadNiveles(materiaId)      // para setear currentMateriaId si lo necesitas
        viewModel.loadPreguntas(nivelId)      // cargar preguntas
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.saveCurrentProgreso() }
    }

    Scaffold(
        topBar = { TopBarback("Preguntas", navigateBack = { navigateBack() }) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val respondidas = ui.respuestas.size
            val total = ui.preguntas.size
            val progress = if (total == 0) 0f else respondidas.toFloat() / total.toFloat()

            Text("Puntaje: ${ui.puntaje}", fontWeight = FontWeight.SemiBold)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(10.dp)
            )
            Text("$respondidas / $total respondidas")

            Spacer(Modifier.height(12.dp))

            if (ui.loadingPreguntas) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            ui.preguntas.forEachIndexed { index, p ->
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
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
