package com.example.udmath.presentation.admin.Contenido.AgregarContenido

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.udmath.presentation.components.TopBarback
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCrearRecursoScreen(
    vm: AdminCrearRecursoViewModel = hiltViewModel(),
    autorId: String,
    moduloInicial: String = "",
    navigateBack: () -> Unit
) {
    val s by vm.state

    LaunchedEffect(moduloInicial) {
        vm.setModuloInicial(moduloInicial)
    }

    val modulos = listOf("Material de apoyo", "Material interesante")

    val tipos = when (s.modulo) {
        "Material de apoyo" -> listOf(
            "programacion",
            "libro",
            "pagina",
            "video"
        )
        "Material interesante" -> listOf(
            "articulo",
            "programacion_int",
            "datos_curiosos",
            "audiovisual"
        )
        else -> emptyList()
    }

    LaunchedEffect(s.modulo) {
        if (s.tipo.isNotBlank() && s.tipo !in tipos) {
            vm.onTipoChange("")
        }
    }

    var expandedTipo by remember { mutableStateOf(false) }
    var expandedModulo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBarback("Agregar Contenido", navigateBack = navigateBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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

            ExposedDropdownMenuBox(
                expanded = expandedModulo,
                onExpandedChange = { expandedModulo = !expandedModulo }
            ) {
                OutlinedTextField(
                    value = s.modulo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Módulo") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedModulo)
                    },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                DropdownMenu(
                    expanded = expandedModulo,
                    onDismissRequest = { expandedModulo = false }
                ) {
                    modulos.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                vm.onModuloChange(opcion)
                                expandedModulo = false

                                val nuevosTipos = when (opcion) {
                                    "Material de apoyo" -> listOf(
                                        "programacion",
                                        "libro",
                                        "pagina",
                                        "video"
                                    )
                                    "Material interesante" -> listOf(
                                        "articulo",
                                        "programacion_int",
                                        "datos_curiosos",
                                        "audiovisual"
                                    )
                                    else -> emptyList()
                                }

                                if (s.tipo !in nuevosTipos) {
                                    vm.onTipoChange("")
                                }
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expandedTipo,
                onExpandedChange = { expandedTipo = !expandedTipo }
            ) {
                OutlinedTextField(
                    value = s.tipo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo)
                    },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                DropdownMenu(
                    expanded = expandedTipo,
                    onDismissRequest = { expandedTipo = false }
                ) {
                    tipos.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                vm.onTipoChange(opcion)
                                expandedTipo = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = s.autor,
                onValueChange = vm::onAutorChange,
                label = { Text("Autor del material") },
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
                label = { Text("URL") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = s.image,
                onValueChange = vm::onImageChange,
                label = { Text("Imagen (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { vm.guardarRecurso(autorId) },
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