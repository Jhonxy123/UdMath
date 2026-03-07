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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.udmath.presentation.Recomendaciones.Components.PreguntaDAD.DragBlankUiState
import com.example.udmath.presentation.Recomendaciones.Components.PreguntaDAD.DragFillBlankQuestion
import com.example.udmath.presentation.components.TopBarback

// ✅ Tipos que deben renderizarse como BOTÓN (no desplegable)
// Agrega más tipos aquí cuando quieras.
private val TIPOS_COMO_BOTON = setOf(
    "tabla_seleccion",
    "crucigrama_naturales"
)

@Composable
fun PreguntasScreen(
    materiaId: String,
    nivelId: String,
    navigateBack: () -> Unit,
    // ✅ Callback para redirigir cuando el tipo es botón (tabla_seleccion u otros del set)
    onNavigateTipoBoton: (tipo: String, preguntaId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MateriaViewModel = hiltViewModel()
) {
    val ui = viewModel.state.value

    // Carga las preguntas cuando el usuario entra a la pantalla
    LaunchedEffect(materiaId, nivelId) {
        viewModel.loadNiveles(materiaId) // para setear currentMateriaId
        viewModel.loadPreguntas(nivelId) // cargar preguntas
    }

    // Cuando el usuario salga de esta pantalla, guarda el progreso
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )

            Text("$respondidas / $total respondidas")
            Spacer(Modifier.height(12.dp))

            if (ui.loadingPreguntas) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            // Lista scrolleable
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(
                    items = ui.preguntas,
                    key = { _, p -> p.id }
                ) { index, p ->

                    val tipo = p.tipo.trim().lowercase()
                    val isCorrect = ui.correctas.contains(p.id)
                    val isIncorrect = ui.incorrectas.contains(p.id)
                    val selectedOption = ui.respuestas[p.id]

                    val headerText =
                        if (tipo == "drag" && !p.texto.contains("____"))
                            "${p.texto} = ____"
                        else
                            p.texto

                    val esTipoBoton = tipo in TIPOS_COMO_BOTON

                    if (esTipoBoton) {

                        val numeroPregunta = index + 1

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateTipoBoton(tipo, p.id) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White // ✅ SIEMPRE blanco
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = "Pregunta $numeroPregunta",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF184998) // azul igual al del card desplegable
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    if (isCorrect) {
                                        Text(
                                            "Correcto ✅",
                                            color = Color(0xFF1B5E20),
                                            fontWeight = FontWeight.Bold
                                        )
                                    } else if (isIncorrect) {
                                        Text(
                                            "Incorrecto ❌",
                                            color = Color(0xFFB71C1C),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = headerText,
                                    color = Color.DarkGray
                                )
                            }
                        }

                    } else {
                        // ✅ Render normal como desplegable
                        PreguntaExpandableCard(
                            preguntaId = p.id,
                            index = index,
                            texto = headerText,
                            headerExtra = {
                                if (isCorrect) {
                                    Text(
                                        "Correcto ✅",
                                        color = Color(0xFF1B5E20),
                                        fontWeight = FontWeight.Bold
                                    )
                                } else if (isIncorrect) {
                                    Text(
                                        "Incorrecto ❌",
                                        color = Color(0xFFB71C1C),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        ) {
                            when (tipo) {
                                "multiple" -> {

                                    Column {

                                        if (p.image.isNotEmpty()) {
                                            Spacer(Modifier.height(8.dp))

                                            AsyncImage(
                                                model = p.image,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(260.dp)   // más espacio
                                                    .clip(RoundedCornerShape(10.dp)),
                                                contentScale = ContentScale.Fit
                                            )

                                            Spacer(Modifier.height(8.dp))
                                        }

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

                                                Text(
                                                    text = opcion,
                                                    color = Color.Black
                                                )
                                            }
                                        }
                                    }
                                }

                                "drag" -> {
                                    val dragUi = ui.dragStates[p.id] ?: DragBlankUiState()

                                    DragFillBlankQuestion(
                                        statement = p.texto,
                                        options = p.opciones,
                                        correctAnswer = p.respuestaCorrecta,
                                        ui = dragUi,
                                        onDropRectChanged = { rect ->
                                            viewModel.onDragDropRectChanged(p.id, rect)
                                        },
                                        onAnswerDropped = { selected, correct ->
                                            viewModel.onDragAnswerDropped(p.id, selected, correct)
                                            viewModel.responderPregunta(p, selected)
                                        },
                                        onClear = {
                                            viewModel.onDragClear(p.id)
                                        }
                                    )
                                }

                                "tf" -> {
                                    val locked = isCorrect || isIncorrect
                                    TrueFalseQuestion(
                                        texto = p.texto,
                                        selectedOption = selectedOption,
                                        enabled = !locked,
                                        onSelect = { opt -> viewModel.responderPregunta(p, opt) }
                                    )
                                }

                                else -> Text("Tipo no soportado: ${p.tipo}")
                            }
                        }
                    }
                }
            }
        }
    }
}
