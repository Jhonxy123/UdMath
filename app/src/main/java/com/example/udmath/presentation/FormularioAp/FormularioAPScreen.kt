package com.example.udmath.presentation.FormularioAp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.udmath.presentation.components.TopBarback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncuestaScreen(
    materias: List<Pair<String, String>>, // (materiaId, nombreVisible)
    isLoading: Boolean,
    onGuardar: (semestre: String, materiaId: String, aprobo: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    // Semestres válidos (ejemplo)
    val semestres = remember {
        listOf("2025-1", "2025-3", "2026-1", "2026-3")
    }

    var semestre by remember { mutableStateOf(semestres.first()) }
    var materiaId by remember { mutableStateOf(materias.firstOrNull()?.first.orEmpty()) }
    var aprobo by remember { mutableStateOf(true) }

    var semestreMenu by remember { mutableStateOf(false) }
    var materiaMenu by remember { mutableStateOf(false) }

    // Validaciones básicas para habilitar botón
    val canSave = semestre.isNotBlank() && materiaId.isNotBlank()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {




        Text(
            text = "Selecciona el semestre y la materia, luego indica si aprobaste o perdiste.",
            style = MaterialTheme.typography.bodyMedium
        )

        // -------- SEMESTRE --------
        ExposedDropdownMenuBox(
            expanded = semestreMenu,
            onExpandedChange = { semestreMenu = !semestreMenu }
        ) {
            OutlinedTextField(
                value = semestre,
                onValueChange = {},
                readOnly = true,
                label = { Text("Semestre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = semestreMenu,
                onDismissRequest = { semestreMenu = false }
            ) {
                semestres.forEach { s ->
                    DropdownMenuItem(
                        text = { Text(s) },
                        onClick = {
                            semestre = s
                            semestreMenu = false
                        }
                    )
                }
            }
        }

        // -------- MATERIA --------
        ExposedDropdownMenuBox(
            expanded = materiaMenu,
            onExpandedChange = { materiaMenu = !materiaMenu }
        ) {
            val materiaNombre = materias.firstOrNull { it.first == materiaId }?.second.orEmpty()

            OutlinedTextField(
                value = materiaNombre,
                onValueChange = {},
                readOnly = true,
                label = { Text("Materia") },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = materiaMenu,
                onDismissRequest = { materiaMenu = false }
            ) {
                materias.forEach { (id, nombre) ->
                    DropdownMenuItem(
                        text = { Text(nombre) },
                        onClick = {
                            materiaId = id
                            materiaMenu = false
                        }
                    )
                }
            }
        }

        // -------- RESULTADO (Radio buttons) --------
        Text("Resultado", style = MaterialTheme.typography.titleMedium)

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = aprobo,
                onClick = { aprobo = true }
            )
            Text("Aprobé")
            Spacer(Modifier.width(16.dp))
            RadioButton(
                selected = !aprobo,
                onClick = { aprobo = false }
            )
            Text("Perdí")
        }

        Spacer(Modifier.height(8.dp))

        // -------- BOTÓN GUARDAR --------
        Button(
            onClick = { onGuardar(semestre, materiaId, aprobo) },
            enabled = canSave && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Guardando...")
            } else {
                Text("Guardar resultado")
            }
        }

        // Nota
        Text(
            text = "Nota: solo se cuentan semestres -1 y -3. El intersemestral (-2) no se registra.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
