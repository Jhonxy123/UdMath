package com.example.udmath.presentation.home.recomendaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecomendacionesScreen() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF004AAD), Color(0xFF1B6CBA))
                    )
                )
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            CardRecomendacion(
                titulo = "Aritmética",
                colorFondo = Color(0xFFFFA451),
                onClick = {
                    // TODO: Navegar a la vista de Aritmética
                    // navController.navigate("aritmetica")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            CardRecomendacion(
                titulo = "Álgebra",
                colorFondo = Color(0xFFFF6F61),
                onClick = {
                    // TODO: Navegar a la vista de Álgebra
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            CardRecomendacion(
                titulo = "Funciones",
                colorFondo = Color(0xFF1EBBA6),
                onClick = {
                    // TODO: Navegar a la vista de Funciones
                }
            )
        }
    }
}


@Composable
fun CardRecomendacion(titulo: String, colorFondo: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() }, // Hace la tarjeta clickeable
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = titulo,
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Ir a $titulo",
                tint = Color.White
            )
        }
    }
}
