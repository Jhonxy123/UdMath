package com.example.udmath.presentation.FormularioAp

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.udmath.presentation.components.TopBarback
import kotlinx.coroutines.launch

@Composable
fun EncuestaRoute(
    navigateBack: () -> Unit,
    viewModel: EncuestaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Mostrar mensajes cuando cambien
    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        val msg = uiState.successMessage ?: uiState.errorMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessages()
        }
    }

    val materias = listOf(
        "algebra" to "Álgebra",
        "aritmetica" to "Aritmética",
        "funciones" to "Funciones"
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {TopBarback(
            "Encuesta de Resultado",
            navigateBack = { navigateBack()}
        )}
    ) { padding ->


        EncuestaScreen(
            materias = materias,
            isLoading = uiState.isLoading,
            onGuardar = { semestre, materiaId, aprobo ->
                viewModel.enviar(semestre, materiaId, aprobo)
            },
            modifier = Modifier.padding(padding),
            navigateBack = { navigateBack() }
        )
    }
}