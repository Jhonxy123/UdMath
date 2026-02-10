package com.example.udmath.presentation.MaterialInteresante.Programacion

import androidx.compose.runtime.Composable
import com.example.udmath.presentation.MaterialInteresante.Components.RecursosScreenInt

@Composable
fun ProgramacionScreen(
    navigateBack: () -> Unit,
) {
    RecursosScreenInt(
        tipo = "programacion",
        navigateBack = { navigateBack() }
    )
}