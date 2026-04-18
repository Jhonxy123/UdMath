package com.example.udmath.presentation.FormularioAp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.udmath.presentation.components.TopBarback
import com.example.udmath.ui.theme.UdMathTheme

@Composable
fun CuestionarioAritmeticaRouteScreen(
    nivelId: String,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBarback(
                "Cuestionario de experiencia",
                navigateBack = { navigateBack() }
            )
        }
    ) { padding ->
        CuestionarioAritmeticaScreen(
            nivelId = nivelId,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun CuestionarioAritmeticaScreen(
    nivelId: String,
    modifier: Modifier = Modifier
) {
    var facilidadUso by remember { mutableIntStateOf(5) }
    var claridadContenido by remember { mutableIntStateOf(5) }
    var ayudoAComprender by remember { mutableStateOf("si") }
    var experienciaGeneral by remember { mutableIntStateOf(5) }
    var mejoraria by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Has completado esta temática de Aritmética. Queremos conocer cómo fue tu experiencia usando el software.",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Temática: $nivelId",
            style = MaterialTheme.typography.titleMedium
        )

        PreguntaEscala(
            titulo = "1. ¿Qué tan fácil te resultó usar el software en esta temática?",
            valor = facilidadUso,
            onValorChange = { facilidadUso = it }
        )

        PreguntaEscala(
            titulo = "2. ¿Qué tan claro te pareció el contenido presentado en el software?",
            valor = claridadContenido,
            onValorChange = { claridadContenido = it }
        )

        Text(
            text = "3. ¿Sientes que el software te ayudó a comprender mejor esta temática?",
            style = MaterialTheme.typography.titleMedium
        )

        OpcionRadio(
            texto = "Sí",
            seleccionado = ayudoAComprender == "si",
            onClick = { ayudoAComprender = "si" }
        )

        OpcionRadio(
            texto = "Más o menos",
            seleccionado = ayudoAComprender == "mas_o_menos",
            onClick = { ayudoAComprender = "mas_o_menos" }
        )

        OpcionRadio(
            texto = "No",
            seleccionado = ayudoAComprender == "no",
            onClick = { ayudoAComprender = "no" }
        )

        PreguntaEscala(
            titulo = "4. ¿Cómo calificarías tu experiencia general usando el software en esta temática?",
            valor = experienciaGeneral,
            onValorChange = { experienciaGeneral = it }
        )

        OutlinedTextField(
            value = mejoraria,
            onValueChange = { mejoraria = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("5. ¿Qué mejorarías del software?") },
            minLines = 3
        )

        Button(
            onClick = {
                // Por ahora no guardamos nada.
                // En el siguiente paso aquí conectamos Firebase/ViewModel.
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar cuestionario")
        }
    }
}

@Composable
private fun PreguntaEscala(
    titulo: String,
    valor: Int,
    onValorChange: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium
        )

        androidx.compose.foundation.layout.Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (1..5).forEach { numero ->
                androidx.compose.foundation.layout.Row {
                    RadioButton(
                        selected = valor == numero,
                        onClick = { onValorChange(numero) }
                    )
                    Text(numero.toString())
                }
            }
        }
    }
}

@Composable
private fun OpcionRadio(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    androidx.compose.foundation.layout.Row {
        RadioButton(
            selected = seleccionado,
            onClick = onClick
        )
        Text(texto)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CuestionarioAritmeticaScreenPreview() {
    UdMathTheme {
        CuestionarioAritmeticaScreen(
            nivelId = "numeros_naturales"
        )
    }
}