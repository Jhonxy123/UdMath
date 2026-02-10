package com.example.udmath.presentation.MaterialInteresante.Components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.udmath.domain.model.Recurso

@Composable
fun RecursosScreenInt(
    tipo: String,               // "Libro", "App", "Artículo", "Video"
    navigateBack: () -> Unit,
    viewModel: RecursosIntViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    var titulo = ""
    LaunchedEffect(tipo) {
        viewModel.load(tipo)
    }

    if(tipo == "libro") titulo = ("Libros")
    if(tipo == "aplicacion") titulo = ("Aplicaciones")
    if(tipo == "video") titulo = ("Videos")


    when {
        state.loading -> Text("Cargando...", color = Color.White)
        state.error != null -> Text("Error: ${state.error}", color = Color.White)
        else -> ListaRecursosScreenInt(
            tituloTopBar = titulo,
            recursos = state.recursos,
            navigateBack = navigateBack
        )
    }
}

