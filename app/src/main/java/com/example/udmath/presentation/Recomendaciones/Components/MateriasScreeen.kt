package com.example.udmath.presentation.Recomendaciones.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

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

    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveCurrentProgreso()
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(blueGradient)
            .verticalScroll(scroll)
            .padding(16.dp),

    ) {
        Spacer(Modifier.height(12.dp))

        // ---- Sección Niveles ----
        Text(
            text = "Niveles",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onclickNivel(nivel.id) },
                    elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 6.dp else 1.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (selected) Icons.Default.CheckCircle else Icons.Default.HelpOutline,
                        contentDescription = null
                    )

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = nivel.titulo,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "Preguntas: ${nivel.totalpreguntas}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (selected) {
                        AssistChip(
                            onClick = { },
                            label = { Text("Seleccionado") }
                        )
                    }
                }
            }
        }
    }
}
