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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CrucigramaScreen() {

    val filas = 7
    val columnas = 7

    val grid = remember {
        List(filas) {
            List(columnas) {
                mutableStateOf("")
            }
        }
    }

    val palabraHorizontal = "LIBRO"
    val palabraVertical = "ESTUDIO"

    var mensaje by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Mini Crucigrama",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🔢 Numeración columnas
        Row {
            Spacer(modifier = Modifier.size(32.dp))
            for (col in 1..columnas) {
                Text(
                    text = col.toString(),
                    modifier = Modifier.width(42.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        // 🧩 Grid
        for (fila in 0 until filas) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                // Número de fila
                Text(
                    text = (fila + 1).toString(),
                    modifier = Modifier.width(32.dp),
                    textAlign = TextAlign.Center
                )

                for (col in 0 until columnas) {

                    if (esCeldaActiva(fila, col)) {

                        Box {

                            BasicTextField(
                                value = grid[fila][col].value,
                                onValueChange = {
                                    if (it.length <= 1) {
                                        grid[fila][col].value = it.uppercase()
                                    }
                                },
                                textStyle = TextStyle(
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier
                                    .size(42.dp)
                                    .border(1.dp, Color.Black)
                                    .background(Color.White)
                                    .padding(4.dp)
                            )

                            // 🔢 Número inicio palabra horizontal (LIBRO)
                            if (fila == 6 && col == 0) {
                                Text(
                                    text = "1",
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(2.dp)
                                )
                            }

                            // 🔢 Número inicio palabra vertical (ESTUDIO)
                            if (fila == 0 && col == 4) {
                                Text(
                                    text = "2",
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(2.dp)
                                )
                            }
                        }

                    } else {

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(Color.LightGray)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            mensaje = if (validar(grid, palabraHorizontal, palabraVertical)) {
                "✅ ¡Correcto!"
            } else {
                "❌ Intenta de nuevo"
            }
        }) {
            Text("Validar")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = mensaje,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        // 📢 Pistas
        Text("Pistas:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Text("1. (Fila 7, Col 1) Objeto para leer (Horizontal)")
        Text("2. (Fila 1, Col 5) Actividad para aprender (Vertical)")
    }
}

fun esCeldaActiva(fila: Int, col: Int): Boolean {

    // LIBRO horizontal en fila 6 columnas 0..4
    if (fila == 6 && col in 0..4) return true

    // ESTUDIO vertical columna 4 filas 0..6
    if (col == 4 && fila in 0..6) return true

    return false
}

fun validar(
    grid: List<List<MutableState<String>>>,
    palabraHorizontal: String,
    palabraVertical: String
): Boolean {

    // Validar LIBRO
    for (i in palabraHorizontal.indices) {
        if (grid[6][i].value != palabraHorizontal[i].toString()) {
            return false
        }
    }

    // Validar ESTUDIO
    for (i in palabraVertical.indices) {
        if (grid[i][4].value != palabraVertical[i].toString()) {
            return false
        }
    }

    return true
}