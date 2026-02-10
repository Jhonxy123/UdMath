package com.example.udmath.presentation.MaterialInteresante.Articulos

import androidx.compose.runtime.Composable
import com.example.udmath.presentation.MaterialApoyo.Component.RecursosScreen
import com.example.udmath.presentation.MaterialInteresante.Components.RecursosScreenInt

@Composable
fun ArticulosScreen(
    navigateBack: () -> Unit,
) {
    RecursosScreenInt(
        tipo = "articulo",
        navigateBack = { navigateBack() }
    )
}