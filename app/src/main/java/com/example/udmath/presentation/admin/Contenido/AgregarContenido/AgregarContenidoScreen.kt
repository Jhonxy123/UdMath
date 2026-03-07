package com.example.udmath.presentation.admin.Contenido.AgregarContenido

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.udmath.presentation.components.TopBarback

@Composable
fun AdminCrearRecursoScreen(
    vm: AdminCrearRecursoViewModel = hiltViewModel(),
    autorId: String,
    autorNombre: String,
    navigateBack: () -> Unit
) {
    val s by vm.state

    Scaffold(
        topBar = {
                TopBarback("Editar Contenido", navigateBack = { navigateBack() })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {

            if (s.errorMessage != null) {
                Text(
                    text = s.errorMessage!!,
                    color = colorScheme.error
                )
            }

            if (s.successMessage != null) {
                Text(text = s.successMessage!!)
            }

            OutlinedTextField(
                value = s.titulo,
                onValueChange = vm::onTituloChange,
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = s.tipo,
                onValueChange = vm::onTipoChange,
                label = { Text("Tipo (video/pdf/link)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = s.modulo,
                onValueChange = vm::onModuloChange,
                label = { Text("Módulo / Materia") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = s.descripcion,
                onValueChange = vm::onDescripcionChange,
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = s.url,
                onValueChange = vm::onUrlChange,
                label = {  Text("URL") },
                modifier =  Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = s.image,
                onValueChange = vm::onImageChange,
                label = { Text("Imagen (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { vm.guardarRecurso(autorId, autorNombre) },
                enabled = !s.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (s.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Guardar recurso")
            }
        }
    }
}