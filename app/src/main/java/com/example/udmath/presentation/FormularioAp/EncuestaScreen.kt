package com.example.udmath.presentation.FormularioAp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun EncuestaScreenPreview() {

    val materias = listOf(
        "algebra" to "Álgebra",
        "aritmetica" to "Aritmética",
        "funciones" to "Funciones"
    )

    EncuestaScreen(
        materias = materias,
        onGuardar = { semestre, materiaId, aprobo ->
            println("$semestre - $materiaId - $aprobo")
        }
    )
}

