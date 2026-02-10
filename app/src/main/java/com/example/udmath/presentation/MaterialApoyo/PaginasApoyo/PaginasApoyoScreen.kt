package com.example.udmath.presentation.MaterialApoyo.PaginasApoyo

import androidx.compose.runtime.Composable
import com.example.udmath.presentation.MaterialApoyo.Component.RecursosScreen

@Composable
fun PaginasApoyoScreen(
    navigateBack: () -> Unit,
) {
    RecursosScreen(
        tipo = "apoyo",
        navigateBack = { navigateBack() }
    )
}