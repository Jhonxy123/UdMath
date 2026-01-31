package com.example.udmath.presentation.MaterialApoyo.Component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.udmath.domain.model.Recurso
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun RecursoCard(
    recurso: Recurso,
    onClick: () -> Unit = {}
) {

    var expanded by rememberSaveable(recurso.titulo) { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable {
                expanded = !expanded   // aquí se despliega/contrae
            }
            .animateContentSize() // Anima el cambio de tamaño
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {

            // Titulo
            Text(
                text = recurso.titulo,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF184998)
            )


            Spacer(modifier = Modifier.height(6.dp))


            Text(
                text = recurso.descripcion,
                color = Color.DarkGray,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
            )

            if (expanded) {
                val meta = buildList {
                    if (recurso.autor.isNotBlank()) add("Autor: ${recurso.autor}")
                    if (recurso.tipo.isNotBlank()) add("Tipo: ${recurso.tipo}")
                    if (recurso.modulo.isNotBlank()) add("Módulo: ${recurso.modulo}")
                }.joinToString(" • ")

                if (meta.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = meta, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Si quieres un click extra para abrir URL/detalle:
                // Text("Abrir", modifier = Modifier.clickable { onClick() }, color = Color(0xFF184998))
            }

            val meta = buildList {
                recurso.autor?.let { add("Autor: $it") }
                recurso.tipo.takeIf { it.isNotBlank() }?.let { add("Tipo: $it") }
            }.joinToString(" • ")

            if (meta.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = meta, color = Color.Gray)
            }



        }

        // El icono que muestra si esta desplegado o contraido
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = if (expanded) "Contraer" else "Expandir",
            tint = Color(0xFF184998),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
