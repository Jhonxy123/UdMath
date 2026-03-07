package com.example.udmath.presentation.admin.Contenido


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.udmath.domain.model.Recurso
import com.example.udmath.presentation.MaterialApoyo.Component.ListaRecursosScreen

@Composable
fun AdminRecursosScreen(
    modulo: String,
    navigateBack: () -> Unit,
    onEditarClick: (Recurso) -> Unit,
    onAgregarClick: () -> Unit,
    viewModel: AdminRecursosViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    var recursoAEliminar by remember { mutableStateOf<Recurso?>(null) }

    LaunchedEffect(modulo) {
        viewModel.loadByModulo(modulo)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->

        ListaRecursosScreen(
            tituloTopBar = modulo,
            recursos = state.recursos,
            navigateBack = navigateBack
        ) { item ->
            AdminRecursoCard(
                recurso = item,
                onEditarClick = { recurso ->
                    onEditarClick(recurso)
                },
                onEliminarClick = { recurso ->
                    recursoAEliminar = recurso
                }
            )
        }
    }

    recursoAEliminar?.let { recurso ->
        AlertDialog(
            onDismissRequest = { recursoAEliminar = null },
            title = { Text("Eliminar recurso") },
            text = { Text("¿Deseas eliminar \"${recurso.titulo}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarRecurso(recurso.id, recurso.modulo)
                        recursoAEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { recursoAEliminar = null }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}