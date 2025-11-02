package com.example.udmath.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.udmath.presentation.components.NavigationDrawer
import com.example.udmath.presentation.components.TopBar
import kotlinx.coroutines.launch
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.udmath.R
import com.example.udmath.ui.theme.white

@Preview
@Composable
fun HomeScreen() {

    // 🔹 Estado del Drawer (menú lateral)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // 🔹 Alcance para ejecutar corrutinas (abrir/cerrar el Drawer)
    val scope = rememberCoroutineScope()

    // 🔹 Creación de un fondo degradado (de azul claro a azul oscuro)
    val blueGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3980C2),  // Azul claro (parte superior)
            Color(0xFF184998)   // Azul oscuro (parte inferior)
        )
    )

    // 🔹 Contenedor principal que incluye el Drawer lateral
    ModalNavigationDrawer(
        drawerState = drawerState, // Estado del Drawer
        drawerContent = {
            // 🔹 Contenido del menú lateral
            ModalDrawerSheet {
                NavigationDrawer() // Aquí va tu componente de navegación lateral
            }
        }
    ) {
        // 🔹 Box principal que ocupa toda la pantalla y aplica el fondo degradado
        Box(
            modifier = Modifier
                .background(blueGradient) // Aplica el fondo con gradiente
                .fillMaxSize()             // Asegura que ocupe todo el espacio disponible
        ) {
            // 🔹 Scaffold: estructura base que contiene la TopBar y el contenido principal
            Scaffold(
                containerColor = Color.Transparent, // Evita que el Scaffold tape el fondo
                topBar = {
                    // 🔹 Barra superior personalizada (TopBar)
                    TopBar(
                        onDrawerClicked = {
                            // Cuando se hace clic en el icono del Drawer, se abre
                            scope.launch {
                                drawerState.open()
                            }
                        }
                    )
                }
            ) { paddingValues ->
                // 🔹 Contenedor para el contenido principal de la pantalla
                Box(
                    modifier = Modifier
                        .padding(paddingValues) // Respeta el espacio de la TopBar
                        .fillMaxSize()          // Ocupa todo el espacio restante
                ) {
                    Column(
                        //horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()) {
                        // 🔹 Aquí va el contenido principal de tu pantalla

                        Spacer(modifier = Modifier.fillMaxWidth().height(50.dp))

                        Image(
                            painter = painterResource(id = R.drawable.logo_inicio),
                            contentDescription = "Logo",
                            Modifier.size(500.dp)
                        )

                        Text(
                            modifier = Modifier.padding(start = 20.dp),
                            textAlign = TextAlign.Left,
                            text = "UdMath",
                            fontWeight = FontWeight.Bold,
                            color = white
                        )

                        Text(
                            modifier = Modifier.padding(start = 20.dp),
                            text = "Bienvenido a UdMath usuario, esta aplicación ha sido diseñada para ayudarte en " +
                                    "el recorrido de tus materias de ciencias basicas, " +
                                    "para comenzar selecciona en la parte inferior la sección a la que desea ingresar. ",
                            color = Color.White // Color del texto para resaltar sobre el fondo azul
                        )
                    }
                }
            }
        }
    }
}
