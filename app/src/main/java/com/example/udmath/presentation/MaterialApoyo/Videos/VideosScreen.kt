package com.example.udmath.presentation.MaterialApoyo.Videos

import androidx.compose.runtime.Composable
import com.example.udmath.presentation.MaterialApoyo.Component.RecursosScreen

@Composable
fun VideosScreen(
    navigateBack: () -> Unit
) {
    RecursosScreen(
        tipo = "video",
        navigateBack = { navigateBack() }
    )
}