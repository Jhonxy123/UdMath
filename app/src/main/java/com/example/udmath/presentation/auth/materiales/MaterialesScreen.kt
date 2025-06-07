package com.example.udmath.presentation.auth.materiales

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
fun MaterialApoyoScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Material de Apoyo") },
                actions = {
                    IconButton(onClick = { /* Menú */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Check, contentDescription = "Tareas") },
                    label = { Text("Tareas") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "Apoyo") },
                    label = { Text("Apoyo") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "Energía") },
                    label = { Text("Energía") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "Progreso") },
                    label = { Text("Progreso") }
                )
            }
        }
    ) { paddingValues ->
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

            // Simulación del arco usando Column y alineación manual
            val items = listOf(
                "Aplicaciones" to null,
                "Libros" to null,
                "Páginas de Apoyo" to null,
                "Tutorías de UD" to null,
                "Videos" to null
            )

            items.forEachIndexed { index, pair ->
                val (titulo, iconRes) = pair

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                        .offset(x = (index * 10).dp, y = 0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        // Si quisieras mostrar un ícono por defecto, podrías usar:
                        // Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.align(Alignment.Center))
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