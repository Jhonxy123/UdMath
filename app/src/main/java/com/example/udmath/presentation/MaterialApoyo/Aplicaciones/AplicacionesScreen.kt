package com.example.udmath.presentation.MaterialApoyo.Aplicaciones

import androidx.compose.runtime.Composable
import com.example.udmath.presentation.MaterialApoyo.Component.RecursosScreen

@Composable
fun AplicacionesScreen(
    navigateBack: () -> Unit,
) {
    RecursosScreen(
        tipo = "aplicacion",
        navigateBack = { navigateBack() }
    )
}