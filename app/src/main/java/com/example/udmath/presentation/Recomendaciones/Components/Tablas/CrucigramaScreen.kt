package com.example.udmath.presentation.Recomendaciones.Components.Tablas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udmath.presentation.components.TopBarback

private enum class Dir { H, V_DOWN, V_UP }

private data class Entry(
    val number: Int,
    val answer: String,
    val startRow: Int,
    val startCol: Int,
    val dir: Dir
)

@Composable
fun CrucigramaMcmMcdScreen(
    preguntaId: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onGameResult: (Boolean) -> Unit = {}
) {
    val filas = 4
    val columnas = 7
    val cellSize = 42.dp

    val entries = remember {
        listOf(
            Entry(1, "10",  startRow = 0, startCol = 2, dir = Dir.V_DOWN),
            Entry(5, "24",  startRow = 0, startCol = 4, dir = Dir.H),
            Entry(6, "105", startRow = 1, startCol = 1, dir = Dir.H),
            Entry(3, "180", startRow = 1, startCol = 1, dir = Dir.V_DOWN),
            Entry(2, "18",  startRow = 2, startCol = 0, dir = Dir.H),
            Entry(4, "15",  startRow = 2, startCol = 3, dir = Dir.V_UP)
        )
    }

    val activeCells = remember {
        setOf(
            0 to 2,
            0 to 4, 0 to 5,

            1 to 1, 1 to 2, 1 to 3,

            2 to 0, 2 to 1,
            2 to 3,

            3 to 1
        )
    }

    val grid = remember {
        List(filas) { List(columnas) { mutableStateOf("") } }
    }

    var mensaje by remember { mutableStateOf<String?>(null) }
    var validationOk by remember { mutableStateOf<Boolean?>(null) }

    Scaffold(
        topBar = { TopBarback("Crucigrama", navigateBack = { navigateBack() }) }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // (Opcional) Si quieres mostrar el id de la pregunta
            // Text("Pregunta: $preguntaId", fontSize = 12.sp, color = Color.Gray)

            Text("Crucigrama MCD / mcm", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            Box {
                CrosswordGrid(
                    filas = filas,
                    columnas = columnas,
                    cellSize = cellSize,
                    grid = grid,
                    activeCells = activeCells
                )

                entries.forEach { e ->
                    ClueLabel(entry = e, cellSize = cellSize)
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val ganoJuego = validateAll(grid, entries)
                    validationOk = ganoJuego
                    mensaje = if (ganoJuego) "✅ ¡Correcto!" else "❌ Intenta de nuevo"

                    // ✅ esto es lo que te permite guardar en Firebase como hiciste en la tabla
                    onGameResult(ganoJuego)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Validar")
            }

            if (mensaje != null) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = mensaje!!,
                    fontWeight = FontWeight.Bold,
                    color = when (validationOk) {
                        true -> MaterialTheme.colorScheme.primary
                        false -> MaterialTheme.colorScheme.error
                        null -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }

            Spacer(Modifier.height(20.dp))
            Text("Pistas:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Text("1. M.C.D. (20,70)")
            Text("2. M.C.D. (72,90)")
            Text("3. m.c.m. (9,15,4)")
            Text("4. M.C.D. (30,45)")
            Text("5. m.c.m. (8,12)")
            Text("6. m.c.m. (3,7,5)")
        }
    }
}

@Composable
private fun CrosswordGrid(
    filas: Int,
    columnas: Int,
    cellSize: Dp,
    grid: List<List<MutableState<String>>>,
    activeCells: Set<Pair<Int, Int>>
) {
    Column {
        for (r in 0 until filas) {
            Row {
                for (c in 0 until columnas) {
                    val active = activeCells.contains(r to c)
                    if (!active) {
                        Box(
                            modifier = Modifier
                                .size(cellSize)
                                .background(Color.Transparent)
                        )
                    } else {
                        BasicTextField(
                            value = grid[r][c].value,
                            onValueChange = { input ->
                                if (input.length <= 1) {
                                    grid[r][c].value = input.filter { it.isDigit() }
                                }
                            },
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .size(cellSize)
                                .border(1.dp, Color.Black)
                                .background(Color.White)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ClueLabel(entry: Entry, cellSize: Dp) {
    val arrow = when (entry.dir) {
        Dir.H -> "→"
        Dir.V_DOWN -> "↓"
        Dir.V_UP -> "↑"
    }

    val baseX = cellSize * entry.startCol
    val baseY = cellSize * entry.startRow

    val (dx, dy) = when (entry.dir) {
        Dir.H -> (-26).dp to 8.dp
        Dir.V_DOWN -> 8.dp to (-22).dp
        Dir.V_UP -> 8.dp to (cellSize + 2.dp)
    }

    Box(modifier = Modifier.offset(x = baseX + dx, y = baseY + dy)) {
        Text(
            text = "${entry.number}. $arrow",
            color = Color(0xFFD32F2F),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun validateAll(
    grid: List<List<MutableState<String>>>,
    entries: List<Entry>
): Boolean {
    for (e in entries) {
        for (i in e.answer.indices) {
            val (r, c) = when (e.dir) {
                Dir.H -> e.startRow to (e.startCol + i)
                Dir.V_DOWN -> (e.startRow + i) to e.startCol
                Dir.V_UP -> (e.startRow - i) to e.startCol
            }
            if (grid[r][c].value != e.answer[i].toString()) return false
        }
    }
    return true
}

private operator fun Dp.times(i: Int): Dp = (this.value * i).dp
private operator fun Dp.plus(other: Dp): Dp = (this.value + other.value).dp