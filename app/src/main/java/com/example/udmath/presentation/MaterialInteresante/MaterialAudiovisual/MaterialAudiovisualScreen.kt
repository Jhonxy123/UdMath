package com.example.udmath.presentation.MaterialInteresante.MaterialAudiovisual

import androidx.compose.runtime.Composable
import com.example.udmath.presentation.MaterialInteresante.Components.RecursosScreenInt

@Composable
fun MaterialAudiovisualScreen(
    navigateBack: () -> Unit,
) {
    RecursosScreenInt(
        tipo = "material_audiovisual",
        navigateBack = { navigateBack() }
    )
}