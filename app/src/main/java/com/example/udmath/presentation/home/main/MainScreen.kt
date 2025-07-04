package com.example.udmath.presentation.home.main

import com.example.udmath.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {

            // ✅ FONDO SIN PADDING
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF004AAD),
                                Color(0xFF1B6CBA)
                            ),
                            startY = 0f,
                            endY = 1600f // Ajusta si necesitas
                        )
                    )
            )

            // ✅ CONTENIDO CON paddingValues para respetar topBar y bottomBar
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // <-- este es el único padding real necesario
                    .padding(horizontal = 16.dp) // opcional
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                /*Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                ) {*/
                    Icon(
                        painter = painterResource(id = R.drawable.ic_main2),//aca
                        contentDescription = "Pizarra",
                        tint = Color.Unspecified,
                        modifier = Modifier.fillMaxSize().size(200.dp)//size(100.dp)
                    )
                /*}*/

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "UdMath",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Bienvenido a UdMath, esta aplicación ha sido diseñada para ayudarte. Para comenzar, selecciona en la parte inferior la sección a la que deseas ingresar.",
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



