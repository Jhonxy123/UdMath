package com.example.udmath.presentation.Recomendaciones.Funciones

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.udmath.presentation.Recomendaciones.Components.MateriasScreen
import com.example.udmath.presentation.components.TopBarStd
import com.example.udmath.presentation.components.TopBarback

@Composable
fun FuncionesScreen(
    navigateBack: () -> Unit,
    navigateToPreguntas: (materiaId: String, nivelId: String) -> Unit
) {
    Scaffold(
        topBar = { TopBarback("Funciones", navigateBack = { navigateBack() }) }
    ) { padding ->
        MateriasScreen(
            materia = "funciones",
            modifier = Modifier.padding(padding),
            onclickNivel = { nivelId ->
                navigateToPreguntas("funciones", nivelId)
            }
        )
    }
}