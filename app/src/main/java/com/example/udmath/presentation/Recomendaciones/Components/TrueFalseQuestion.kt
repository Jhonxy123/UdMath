package com.example.udmath.presentation.Recomendaciones.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun TrueFalseQuestion(
    texto: String,
    image: String,
    selectedOption: String?,
    enabled: Boolean,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        if (image.isNotEmpty()) {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Fit
            )
        }

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

