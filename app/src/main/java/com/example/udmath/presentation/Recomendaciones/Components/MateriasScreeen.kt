package com.example.udmath.presentation.Recomendaciones.Components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.math.roundToInt

@Composable
fun MateriasScreen(
    materia: String = "",
    modifier: Modifier = Modifier,
    onclickNivel: (nivelID: String) -> Unit,
    viewModel: MateriaViewModel = hiltViewModel()
) {
    val ui = viewModel.state.value

    LaunchedEffect(materia) {
        viewModel.loadNiveles(materia)
    }

    val scroll = rememberScrollState()
    val blueGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3980C2),
            Color(0xFF184998)
        )
    )

    val progresoGeneral = if (ui.totalPreguntasMateria == 0) 0f
    else ui.totalCorrectasMateria.toFloat() / ui.totalPreguntasMateria.toFloat()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(blueGradient)
            .verticalScroll(scroll)
            .padding(16.dp),
    ) {
        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Progreso total",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF184998)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "${(progresoGeneral * 100).roundToInt()}% completado correctamente",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progresoGeneral },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "${ui.totalCorrectasMateria}/${ui.totalPreguntasMateria} preguntas correctas · ${ui.totalRespondidasMateria} respondidas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        Text(
            text = "Temas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
        Spacer(Modifier.height(8.dp))

        if (ui.loadingNiveles) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
            Spacer(Modifier.height(12.dp))
        }

        ui.error?.let { err ->
            if (err.isNotBlank()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
            }
        }

        ui.niveles.forEach { nivel ->
            val selected = ui.selectedNivelId == nivel.id
            val progresoNivel = ui.progresoPorNivel[nivel.id] ?: NivelProgressUi(total = nivel.totalpreguntas)
            val completado = progresoNivel.completadoCorrectamente

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onclickNivel(nivel.id) },
                elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 6.dp else 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        completado -> Color(0xFFE8F5E9)
                        selected -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (completado || selected) Icons.Default.CheckCircle else Icons.Default.HelpOutline,
                        contentDescription = null,
                        tint = if (completado) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = nivel.titulo,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (completado) Color(0xFF1B5E20) else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${(progresoNivel.porcentajeCorrecto * 100).roundToInt()}% · ${progresoNivel.correctas}/${progresoNivel.total} correctas",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { progresoNivel.porcentajeCorrecto },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = if (completado) {
                                "Tema completado correctamente"
                            } else {
                                "Respondidas: ${progresoNivel.respondidas}/${progresoNivel.total}"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = if (completado) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
