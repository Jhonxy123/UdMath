package com.example.udmath.presentation.MaterialApoyo.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.udmath.domain.model.Recurso

@Composable
fun RecursoCard(
    recurso: Recurso,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = recurso.titulo,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF184998)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = recurso.descripcion,
                color = Color.DarkGray
            )

            val meta = buildList {
                recurso.autor?.let { add("Autor: $it") }
                recurso.tipo.takeIf { it.isNotBlank() }?.let { add("Tipo: $it") }
            }.joinToString(" • ")

            if (meta.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = meta, color = Color.Gray)
            }
        }
    }
}
