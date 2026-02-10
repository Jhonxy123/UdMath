package com.example.udmath.presentation.MaterialInteresante.DatosCuriosos

import androidx.compose.runtime.Composable
import com.example.udmath.presentation.MaterialInteresante.Components.RecursosScreenInt

@Composable
fun DatosCuriososScreen(
    navigateBack: () -> Unit,
) {
    RecursosScreenInt(
        tipo = "datos_curiosos",
        navigateBack = { navigateBack() }
    )
}