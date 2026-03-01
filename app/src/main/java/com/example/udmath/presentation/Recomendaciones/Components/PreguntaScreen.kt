package com.example.udmath.presentation.Recomendaciones.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

                val isCorrect = ui.correctas.contains(p.id)
                val isIncorrect = ui.incorrectas.contains(p.id)
                val selectedOption = ui.respuestas[p.id]

                PreguntaExpandableCard(
                    preguntaId = p.id,
                    index = index,
                    texto = p.texto,
                    headerExtra = {
                        if (isCorrect) {
                            Text("Correcto ✅", color = Color(0xFF1B5E20), fontWeight = FontWeight.Bold)
                        } else if (isIncorrect) {
                            Text("Incorrecto ❌", color = Color(0xFFB71C1C), fontWeight = FontWeight.Bold)
                        }
                    }
                ) {
                    when (p.tipo.trim().lowercase()) {

                        "multiple" -> {
                            // contenido de multiple dentro del card
                            p.opciones.forEach { opcion ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { viewModel.responderPregunta(p, opcion) }
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedOption == opcion,
                                        onClick = { viewModel.responderPregunta(p, opcion) }
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(text = opcion, color = Color.Black)
                                }
                            }
                        }

                        "drag" -> {
                            DragFillBlankQuestion(
                                statement = p.texto, // ejemplo "2 + 2 = ____"
                                options = p.opciones,
                                correctAnswer = p.respuestaCorrecta,
                                onAnswered = { selected, _ ->
                                    viewModel.responderPregunta(p, selected)
                                }
                            )
                        }

                        "tf" -> {
                            val selectedOption = ui.respuestas[p.id]
                            val locked = ui.correctas.contains(p.id) || ui.incorrectas.contains(p.id)

                            TrueFalseQuestion(
                                texto = p.texto,
                                selectedOption = selectedOption,
                                enabled = !locked,
                                onSelect = { opt ->
                                    viewModel.responderPregunta(p, opt)
                                }
                            )
                        }

                        else -> Text("Tipo no soportado: ${p.tipo}")
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
