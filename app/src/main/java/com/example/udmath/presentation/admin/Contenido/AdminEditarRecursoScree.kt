package com.example.udmath.presentation.admin.Contenido


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.udmath.domain.model.Recurso
import com.example.udmath.presentation.components.TopBarback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditarRecursoScreen(
    recursoId: String,
    navigateBack: () -> Unit,
    viewModel: AdminEditarRecursoViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    LaunchedEffect(recursoId) {
        viewModel.loadRecurso(recursoId)
    }

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var modulo by remember { mutableStateOf("") }

    LaunchedEffect(state.recurso) {
        state.recurso?.let { recurso ->
            titulo = recurso.titulo
            descripcion = recurso.descripcion
            url = recurso.url
            imagen = recurso.imagen
            autor = recurso.autor
            tipo = recurso.tipo
            modulo = recurso.modulo
        }
    }

    LaunchedEffect(state.success) {
        if (state.success) {
            navigateBack()
        }
    }

    val blueGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3980C2),
            Color(0xFF184998)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(blueGradient)
    ) {
        TopBarback("Editar recurso", navigateBack)

        when {
            state.loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null && state.recurso == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error: ${state.error}", color = Color.White)
                }
            }

            else -> {
                val modulos = listOf("Material de apoyo", "Material interesante")

                val tipos = when (modulo) {
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

                LaunchedEffect(modulo) {
                    if (tipo.isNotBlank() && tipo !in tipos) {
                        tipo = ""
                    }
                }

                var expandedTipo by remember { mutableStateOf(false) }
                var expandedModulo by remember { mutableStateOf(false) }


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    OutlinedTextField(
                        value = autor,
                        onValueChange = { autor = it },
                        label = { Text("Autor") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = url,
                        onValueChange = { url = it },
                        label = { Text("URL") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = imagen,
                        onValueChange = { imagen = it },
                        label = { Text("URL imagen") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedTipo,
                        onExpandedChange = { expandedTipo = !expandedTipo }
                    ) {
                        OutlinedTextField(
                            value = tipo,
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
                                        tipo = opcion
                                        expandedTipo = false
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedModulo,
                        onExpandedChange = { expandedModulo = !expandedModulo }
                    ) {
                        OutlinedTextField(
                            value = modulo,
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
                                        modulo = opcion
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

                                        if (tipo !in nuevosTipos) {
                                            tipo = ""
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val recursoActual = state.recurso ?: return@Button

                            viewModel.guardarCambios(
                                Recurso(
                                    id = recursoActual.id,
                                    titulo = titulo,
                                    descripcion = descripcion,
                                    url = url,
                                    imagen = imagen,
                                    autor = autor,
                                    tipo = tipo,
                                    modulo = modulo,
                                    autorId = recursoActual.autorId,
                                    fecha_agregado = recursoActual.fecha_agregado
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.saving
                    ) {
                        Text(if (state.saving) "Guardando..." else "Guardar cambios")
                    }

                    state.error?.let {
                        Text(text = it, color = Color.Red)
                    }
                }
            }
        }
    }
}