package com.example.udmath.presentation.Recomendaciones.Components.Tablas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udmath.presentation.components.TopBarback

@Composable
fun TablaNaturalesScreen(
    preguntaId: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onGameResult: (Boolean) -> Unit = {}
) {
    // 8 columnas visibles
    val columnas = listOf(2, 3, 4, 5, 6, 8)

    // 5 números a la izquierda
    val filas = listOf(40, 120, 54, 81, 70)

    // Estado: X marcada por cada celda (filaIndex, colIndex)
    val marks = remember {
        mutableStateListOf<Boolean>().apply {
            repeat(filas.size * columnas.size) { add(false) }
        }
    }

    fun indexOf(row: Int, col: Int) = row * columnas.size + col

    var validationMessage by remember { mutableStateOf<String?>(null) }
    var validationOk by remember { mutableStateOf<Boolean?>(null) }

    fun ganoTabla(): Boolean {
        for (r in filas.indices) {
            for (c in columnas.indices) {
                val idx = indexOf(r, c)
                val esDivisible = (filas[r] % columnas[c] == 0)
                val marcado = marks[idx]
                if (marcado != esDivisible) return false
            }
        }
        return true
    }

    // ✅ TOPBAR igual a PreguntasScreen
    Scaffold(
        topBar = { TopBarback("Tabla", navigateBack = { navigateBack() }) }
    ) { innerPadding ->

        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Completa la tabla marcando con una X la casilla si el número de la izquierda es divisible por el número de arriba.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(16.dp))

                DivisibilidadTableInteractive(
                    filas = filas,
                    columnas = columnas,
                    marks = marks,
                    indexOf = { r, c -> indexOf(r, c) }
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        val totalMarcadas = marks.count { it }
                        if (totalMarcadas == 0) {
                            val ganoJuego = false
                            validationOk = false
                            validationMessage = "⚠️ Marca al menos una casilla antes de validar."
                            onGameResult(ganoJuego)
                            return@Button
                        }

                        val ganoJuego = ganoTabla()

                        validationOk = ganoJuego
                        validationMessage = if (ganoJuego) {
                            "✅ ¡Ganaste! La tabla está perfecta."
                        } else {
                            "❌ Aún no ganas. Revisa tus marcas."
                        }

                        onGameResult(ganoJuego)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Validar")
                }

                if (validationMessage != null) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = validationMessage!!,
                        style = MaterialTheme.typography.bodyLarge,
                        color = when (validationOk) {
                            true -> MaterialTheme.colorScheme.primary
                            false -> MaterialTheme.colorScheme.error
                            null -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DivisibilidadTableInteractive(
    filas: List<Int>,
    columnas: List<Int>,
    marks: MutableList<Boolean>,
    indexOf: (rowIndex: Int, colIndex: Int) -> Int,
    modifier: Modifier = Modifier
) {
    val cellSize = 44.dp
    val headerSize = 64.dp
    val border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)

    Column(modifier = modifier) {
        // Encabezados superiores
        Row(verticalAlignment = Alignment.CenterVertically) {
            Cell(
                text = "",
                width = headerSize,
                height = cellSize,
                border = border,
                bold = true
            )
            columnas.forEach { col ->
                Cell(
                    text = col.toString(),
                    width = cellSize,
                    height = cellSize,
                    border = border,
                    bold = true
                )
            }
        }

        // Filas + celdas clicables
        filas.forEachIndexed { r, fila ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Encabezado izquierdo
                Cell(
                    text = fila.toString(),
                    width = headerSize,
                    height = cellSize,
                    border = border,
                    bold = true
                )

                columnas.forEachIndexed { c, _ ->
                    val idx = indexOf(r, c)
                    val marked = marks[idx]
                    ClickableCell(
                        text = if (marked) "X" else "",
                        width = cellSize,
                        height = cellSize,
                        border = border,
                        onToggle = { marks[idx] = !marks[idx] }
                    )
                }
            }
        }
    }
}

@Composable
private fun Cell(
    text: String,
    width: Dp,
    height: Dp,
    border: BorderStroke,
    bold: Boolean = false
) {
    val textColor = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .border(border),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp,
                fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
                color = textColor
            )
        )
    }
}

@Composable
private fun ClickableCell(
    text: String,
    width: Dp,
    height: Dp,
    border: BorderStroke,
    onToggle: () -> Unit
) {
    val textColor = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .border(border)
            .clickable(role = Role.Button) { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
    }
}
