package com.example.udmath.presentation.Recomendaciones.Algebra

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.udmath.presentation.Recomendaciones.Components.MateriasScreen
import com.example.udmath.presentation.components.TopBarback

@Composable
fun AlgebraScreen(
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = { TopBarback("Algebra", navigateBack = { navigateBack() }) }
    ) { innerPadding ->
        MateriasScreen(
            materia = "algebra",
            modifier = Modifier.padding(innerPadding)
        )
    }
}


