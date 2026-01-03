package com.example.udmath.presentation.MaterialApoyo

import androidx.compose.foundation.Image
import com.example.udmath.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import com.example.udmath.presentation.components.NavigationDrawer
import com.example.udmath.presentation.components.TopBar
import com.example.udmath.presentation.components.blueGradient
import com.example.udmath.ui.theme.white
import kotlinx.coroutines.launch

// import androidx.navigation.NavController // ← Descomenta esto si usas Navigation


@Composable
// fun MaterialInteresanteScreen(navController: NavController) { // ← cuando uses navegación
fun MaterialApoyoScreen(){

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .background(blueGradient)
            .fillMaxSize()
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopBar(
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.logo_inicio),
                        contentDescription = "Logo",
                        Modifier.size(400.dp)
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
                        color = Color.White
                    )
                }
            }
        }
    }
}