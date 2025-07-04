package com.example.udmath.presentation.home.retos

import com.example.udmath.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource

// import androidx.navigation.NavController // ← Descomenta si usas navegación

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// fun RetosScreen(navController: NavController) {
fun RetosScreen() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF004AAD), Color(0xFF1B6CBA))
                    )
                )
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val retos = listOf(
                Triple("Sudoku", R.drawable.sudoku, "sudoku"),
                Triple("2048", R.drawable._048, "juego_2048"),
                Triple("Cuestionario", R.drawable.cuestionario, "cuestionario")
            )

            retos.forEach { (titulo, icono, ruta) ->
                TarjetaReto(
                    titulo = titulo,
                    iconRes = icono,
                    onClick = {
                        // navController.navigate(ruta) // ← Actívalo cuando tengas rutas
                    }
                )
            }
        }
    }
}

@Composable
fun TarjetaReto(
    titulo: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titulo,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color(0xFF004AAD)
            )

            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = titulo,
                tint = Color.Unspecified,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}
