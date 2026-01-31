package com.example.udmath.presentation.MaterialApoyo.Libros

import androidx.compose.runtime.Composable
import com.example.udmath.presentation.MaterialApoyo.Component.RecursosScreen

@Composable
fun LibrosScreen(
    navigateBack: () -> Unit
) {
    RecursosScreen(
        tipo = "libro",
        navigateBack = { navigateBack() }
    )
}