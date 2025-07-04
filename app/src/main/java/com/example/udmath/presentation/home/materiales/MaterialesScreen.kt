package com.example.udmath.presentation.home.materiales

import com.example.udmath.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// fun MaterialApoyoScreen(navController: NavController) { // ← Usa esto cuando tengas NavController
fun MaterialApoyoScreen() {
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
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Lista de botones con íconos
            val items = listOf(
                Triple("Aplicaciones", R.drawable.aplicaciones, "aplicaciones"),
                Triple("Libros", R.drawable.libros, "libros"),
                Triple("Páginas de Apoyo", R.drawable.apoyo, "apoyo"),
                Triple("Tutorías de UD", R.drawable.tutoria, "tutorias"),
                Triple("Videos", R.drawable.video, "videos")
            )

            items.forEachIndexed { index, (titulo, iconRes, route) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                        .offset(x = (index * 10).dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable {
                                // Descomenta esto cuando tengas NavController y rutas definidas:
                                // navController.navigate(route)
                            }
                    ) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = titulo,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = titulo,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
