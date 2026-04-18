package com.example.udmath.presentation.Recomendaciones.Aritmetica

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.udmath.presentation.Recomendaciones.Components.MateriasScreen
import com.example.udmath.presentation.components.TopBarback

@Composable
fun AritmeticaScreen(
    navigateBack: () -> Unit,
    navigateToPreguntas: (materiaId: String, nivelId: String) -> Unit,
    navigateToCuestionario: (nivelId: String) -> Unit
) {
    Scaffold(
        topBar = { TopBarback("Aritmetica", navigateBack = { navigateBack() }) }
    ) { padding ->
        MateriasScreen(
            materia = "aritmetica",
            modifier = Modifier.padding(padding),
            onclickNivel = { nivelId ->
                navigateToPreguntas("aritmetica", nivelId)
            },
            onAbrirFormulario = { }
        )
    }
}

