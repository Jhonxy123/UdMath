package com.example.udmath.presentation.admin.Graficas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BarPoint(
    val label: String,
    val aprobado: Int,
    val desaprobado: Int
)

@Composable
fun GraficasScreen(
    onDownloadPdf: () -> Unit = {}
) {
    val bg = Color(0xFFF3F3F3)
    val blue = Color(0xFF184998)
    val red = Color(0xFFE53935)

    // 🔧 Datos de ejemplo (como la imagen). Cambia esto por state del ViewModel luego.
    val points = listOf(
        BarPoint("2023-3", aprobado = 85, desaprobado = 55),
        BarPoint("2024-2", aprobado = 55, desaprobado = 85),
        BarPoint("2024-3", aprobado = 60, desaprobado = 40),
        BarPoint("2025-1", aprobado = 85, desaprobado = 88)
    )

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(18.dp))

            Text(
                text = "Gráficas",
                fontWeight = FontWeight.SemiBold,
                color = blue,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(12.dp))

            InfoCard(
                text = "Desde de aquí podrás ver los reportes que\n" +
                        "generan los usuarios con respecto a la\n" +
                        "aprobación de sus espacios académicos.",
                textColor = blue
            )

            Spacer(Modifier.height(18.dp))

            ChartCard(
                title = "Cálculo Integral",
                borderColor = blue,
                aprobadoColor = blue,
                desaprobadoColor = red,
                points = points
            )

            Spacer(Modifier.height(26.dp))

            ShadowButton(
                text = "Descargar en PDF",
                onClick = onDownloadPdf,
                textColor = blue
            )

            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun InfoCard(
    text: String,
    textColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDEDED)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            modifier = Modifier.padding(14.dp)
        )
    }
}

@Composable
private fun ChartCard(
    title: String,
    borderColor: Color,
    aprobadoColor: Color,
    desaprobadoColor: Color,
    points: List<BarPoint>
) {
    val shape = RoundedCornerShape(18.dp)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = borderColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(10.dp))

            Divider(color = borderColor, thickness = 1.dp)

            Spacer(Modifier.height(10.dp))

            BarChart(
                points = points,
                gridColor = borderColor,
                aprobadoColor = aprobadoColor,
                desaprobadoColor = desaprobadoColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            )

            Spacer(Modifier.height(10.dp))

            Legend(
                aprobadoColor = aprobadoColor,
                desaprobadoColor = desaprobadoColor,
                textColor = borderColor
            )
        }
    }
}

@Composable
private fun Legend(
    aprobadoColor: Color,
    desaprobadoColor: Color,
    textColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem(color = aprobadoColor, label = "Aprobado", textColor = textColor)
        LegendItem(color = desaprobadoColor, label = "Desaprobado", textColor = textColor)
    }
}

@Composable
private fun LegendItem(color: Color, label: String, textColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.width(8.dp))
        Text(label, color = textColor, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}

@Composable
private fun BarChart(
    points: List<BarPoint>,
    gridColor: Color,
    aprobadoColor: Color,
    desaprobadoColor: Color,
    modifier: Modifier = Modifier
) {
    // Escala 0..100 como en la imagen
    val maxY = 100f
    val ySteps = listOf(0, 20, 40, 60, 80, 100)

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val leftPadding = 34f
        val rightPadding = 10f
        val topPadding = 10f
        val bottomPadding = 28f

        val chartW = w - leftPadding - rightPadding
        val chartH = h - topPadding - bottomPadding

        // Grid lines + labels
        ySteps.forEach { v ->
            val y = topPadding + chartH * (1f - (v / maxY))
            drawLine(
                color = gridColor,
                start = Offset(leftPadding, y),
                end = Offset(w - rightPadding, y),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )

            // y labels (simple)
            drawContext.canvas.nativeCanvas.apply {
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.rgb(24, 73, 152)
                    textSize = 24f
                    isAntiAlias = true
                }
                drawText(v.toString(), 0f, y + 8f, paint)
            }
        }

        if (points.isEmpty()) return@Canvas

        // Bars
        val groupCount = points.size
        val groupGap = 14f
        val groupW = (chartW - groupGap * (groupCount - 1)) / groupCount

        val barGap = 8f
        val barW = (groupW - barGap) / 2f

        points.forEachIndexed { i, p ->
            val gx = leftPadding + i * (groupW + groupGap)

            fun barHeight(value: Int): Float {
                return chartH * (value.coerceIn(0, 100) / maxY)
            }

            val aH = barHeight(p.aprobado)
            val dH = barHeight(p.desaprobado)

            // Aprobado (izq)
            drawRect(
                color = aprobadoColor,
                topLeft = Offset(gx, topPadding + (chartH - aH)),
                size = Size(barW, aH)
            )

            // Desaprobado (der)
            drawRect(
                color = desaprobadoColor,
                topLeft = Offset(gx + barW + barGap, topPadding + (chartH - dH)),
                size = Size(barW, dH)
            )

            // X label
            drawContext.canvas.nativeCanvas.apply {
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.rgb(24, 73, 152)
                    textSize = 24f
                    isAntiAlias = true
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                val cx = gx + groupW / 2f
                drawText(p.label, cx, h - 6f, paint)
            }
        }

        // Border around plot area (opcional suave)
        drawRect(
            color = gridColor.copy(alpha = 0.0f),
            topLeft = Offset(leftPadding, topPadding),
            size = Size(chartW, chartH),
            style = Stroke(width = 0f)
        )
    }
}

@Composable
private fun ShadowButton(
    text: String,
    onClick: () -> Unit,
    textColor: Color
) {
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth(0.72f)
            .shadow(12.dp, shape)
    ) {
        Card(
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = text,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
