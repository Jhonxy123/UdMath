package com.example.udmath.presentation.Recomendaciones.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun DragFillBlankQuestion(
    statement: String,                 // ej: "2 + 3 = ____"
    options: List<String>,             // ej: ["4","5","6"]
    correctAnswer: String,             // ej: "5"
    modifier: Modifier = Modifier,
    onAnswered: (selected: String, isCorrect: Boolean) -> Unit = { _, _ -> }
) {
    var dropRect by remember { mutableStateOf<Rect?>(null) }
    var placedAnswer by remember { mutableStateOf<String?>(null) }
    var feedback by remember { mutableStateOf<Boolean?>(null) } // null/true/false

    val locked = feedback == true // si ya es correcta, bloquea (puedes cambiarlo)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Arrastra la respuesta al espacio en blanco",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        // Enunciado + zona de drop
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = statement.replace("____", ""),
                style = MaterialTheme.typography.bodyLarge
            )

            DropZone(
                placedAnswer = placedAnswer,
                feedback = feedback,
                enabled = !locked,
                onRectReady = { dropRect = it },
                onClear = {
                    if (!locked) {
                        placedAnswer = null
                        feedback = null
                    }
                }
            )
        }

        // Opciones arrastrables
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            options.forEach { opt ->
                DraggableChip(
                    text = opt,
                    enabled = !locked && placedAnswer != opt,
                    dropRectProvider = { dropRect },
                    onDroppedInside = {
                        placedAnswer = opt
                        val isCorrect = opt == correctAnswer
                        feedback = isCorrect
                        onAnswered(opt, isCorrect)
                    }
                )
            }
        }

        // Feedback
        when (feedback) {
            true -> Text("✅ Correcto", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            false -> Text("❌ Incorrecto, intenta de nuevo", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
            null -> {}
        }
    }
}

@Composable
private fun DropZone(
    placedAnswer: String?,
    feedback: Boolean?,
    enabled: Boolean,
    onRectReady: (Rect) -> Unit,
    onClear: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val borderColor = when (feedback) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.error
        null -> MaterialTheme.colorScheme.outline
    }

    Box(
        modifier = Modifier
            .height(44.dp)
            .widthIn(min = 110.dp)
            .border(2.dp, borderColor, shape)
            .background(MaterialTheme.colorScheme.surface, shape)
            .onGloballyPositioned { coords ->
                val pos = coords.positionInRoot()
                val size = coords.size
                onRectReady(
                    Rect(
                        left = pos.x,
                        top = pos.y,
                        right = pos.x + size.width,
                        bottom = pos.y + size.height
                    )
                )
            }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (placedAnswer == null) {
            Text("____", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(placedAnswer, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                if (enabled) {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "✕",
                        modifier = Modifier
                            .padding(6.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    // Si quieres que al tocar la X borre, cambia por clickable en el Text.
                    // Para no mezclar click con drag aquí, lo dejamos simple.
                }
            }
        }
    }
}

@Composable
private fun DraggableChip(
    text: String,
    enabled: Boolean,
    dropRectProvider: () -> Rect?,
    onDroppedInside: () -> Unit
) {
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var chipRect by remember { mutableStateOf<Rect?>(null) }
    var isDragging by remember { mutableStateOf(false) }

    // Convertimos offset a IntOffset para Modifier.offset
    val intOffset = IntOffset(dragOffset.x.roundToInt(), dragOffset.y.roundToInt())

    AssistChip(
        onClick = { /* opcional */ },
        enabled = enabled,
        label = { Text(text) },
        modifier = Modifier
            .onGloballyPositioned { coords ->
                val pos = coords.positionInRoot()
                val size = coords.size
                chipRect = Rect(
                    left = pos.x,
                    top = pos.y,
                    right = pos.x + size.width,
                    bottom = pos.y + size.height
                )
            }
            .offset { if (isDragging) intOffset else IntOffset.Zero }
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                detectDragGestures(
                    onDragStart = {
                        isDragging = true
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffset += dragAmount
                    },
                    onDragEnd = {
                        isDragging = false

                        val dropRect = dropRectProvider()
                        val currentChipRect = chipRect

                        // Calculamos el "centro" aproximado del chip ya desplazado
                        if (dropRect != null && currentChipRect != null) {
                            val moved = Rect(
                                left = currentChipRect.left + dragOffset.x,
                                top = currentChipRect.top + dragOffset.y,
                                right = currentChipRect.right + dragOffset.x,
                                bottom = currentChipRect.bottom + dragOffset.y
                            )

                            val center = Offset(
                                (moved.left + moved.right) / 2f,
                                (moved.top + moved.bottom) / 2f
                            )

                            if (dropRect.contains(center)) {
                                onDroppedInside()
                            }
                        }

                        // siempre vuelve a su sitio visualmente
                        dragOffset = Offset.Zero
                    }
                )
            }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DragFillBlankQuestionPreview() {
    MaterialTheme {
        Surface {
            DragFillBlankQuestion(
                statement = "2 + 3 = ____",
                options = listOf("4", "5", "6"),
                correctAnswer = "5"
            )
        }
    }
}
