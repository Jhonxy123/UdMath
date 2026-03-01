package com.example.udmath.presentation.Recomendaciones.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun TrueFalseQuestion(
    texto: String,
    selectedOption: String?,
    enabled: Boolean,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        // Opciones tipo botón para ahorrar espacio
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("Verdadero", "Falso").forEach { opt ->
                val selected = selectedOption == opt

                FilterChip(
                    selected = selected,
                    onClick = { if (enabled) onSelect(opt) },
                    enabled = enabled,
                    label = { Text(opt) }
                )
            }
        }
    }
}