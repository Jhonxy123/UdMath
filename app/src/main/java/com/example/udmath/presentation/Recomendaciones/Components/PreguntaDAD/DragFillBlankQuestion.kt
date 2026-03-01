package com.example.udmath.presentation.Recomendaciones.Components.PreguntaDAD

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun DragFillBlankQuestion(
    // solo para mostrar la parte del blank; el texto completo va en el Card
    statement: String,
    options: List<String>,
    correctAnswer: String,
    ui: DragBlankUiState,
    onDropRectChanged: (Rect) -> Unit,
    onAnswerDropped: (selected: String, correctAnswer: String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val marker = "____"
    val hasMarker = statement.contains(marker)

    // si el texto no trae ____ igual mostramos un " = ____" visualmente
    val leftLabel = if (hasMarker) {
        // nos quedamos con lo que está antes del blank, pero NO lo mostramos para no duplicar
        "="
    } else {
        "="
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        // ✅ fila compacta: " = [DropZone]"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = leftLabel,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            DropZone(
                placedAnswer = ui.placedAnswer,
                feedback = ui.feedback,
                enabled = !ui.locked,
                onRectReady = onDropRectChanged,
                onClear = onClear,
                modifier = Modifier.weight(1f, fill = false) // 👈 evita que se “pierda”
            )
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            options.forEach { opt ->
                DraggableChip(
                    text = opt,
                    enabled = !ui.locked && ui.placedAnswer != opt,
                    dropRect = ui.dropRect,
                    onDroppedInside = { onAnswerDropped(opt, correctAnswer) }
                )
            }
        }

        when (ui.feedback) {
            true -> Text(
                "✅ Correcto",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
            false -> Text(
                "❌ Incorrecto, intenta de nuevo",
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.SemiBold
            )
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
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    val borderColor = when (feedback) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.error
        null -> MaterialTheme.colorScheme.outline
    }

    Box(
        modifier = modifier
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
                    TextButton(onClick = onClear, contentPadding = PaddingValues(0.dp)) {
                        Text("Quitar")
                    }
                }
            }
        }
    }
}

@Composable
private fun DraggableChip(
    text: String,
    enabled: Boolean,
    dropRect: Rect?,
    onDroppedInside: () -> Unit
) {
    var dragOffset = androidx.compose.ui.geometry.Offset.Zero
    var chipRect: Rect? = null
    var isDragging = false

    AssistChip(
        onClick = {},
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
            .offset {
                if (isDragging) IntOffset(dragOffset.x.roundToInt(), dragOffset.y.roundToInt())
                else IntOffset.Zero
            }
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffset += dragAmount
                    },
                    onDragEnd = {
                        isDragging = false

                        val currentChipRect = chipRect
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
                            if (dropRect.contains(center)) onDroppedInside()
                        }

                        dragOffset = Offset.Zero
                    }
                )
            }
    )
}
