package com.example.udmath.presentation.Recomendaciones.Components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.udmath.domain.model.Pregunta

@Composable
fun PreguntaCard(
    pregunta: Pregunta,
    index: Int,
    selectedOption: String?,
    isCorrect: Boolean,
    isIncorrect: Boolean,
    onOpcionSeleccionada: (preguntaId: String, opcion: String) -> Unit
) {
    var expanded by rememberSaveable(pregunta.id) { mutableStateOf(false) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { expanded = !expanded }
            .animateContentSize()
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = "Pregunta ${index + 1}",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF184998)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = pregunta.texto,
                color = Color.DarkGray,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
            )

            if (expanded) {

                if (isCorrect) {
                    Text("Correcto ✅", color = Color(0xFF1B5E20), fontWeight = FontWeight.Bold)
                } else if (isIncorrect) {
                    Text("Incorrecto ❌", color = Color(0xFFB71C1C), fontWeight = FontWeight.Bold)
                }


                Spacer(modifier = Modifier.height(10.dp))

                // Opciones de respuesta (Radio)
                pregunta.opciones.forEach { opcion ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                onOpcionSeleccionada(pregunta.id, opcion)
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedOption == opcion,
                            onClick = { onOpcionSeleccionada(pregunta.id, opcion) }
                        )

                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = opcion,
                            color = Color.Black
                        )
                    }
                }

                if (pregunta.opciones.isEmpty()) {
                    Text(
                        text = "No hay opciones para esta pregunta.",
                        color = Color.Gray
                    )
                }
            }
        }

        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = if (expanded) "Contraer" else "Expandir",
            tint = Color(0xFF184998),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
