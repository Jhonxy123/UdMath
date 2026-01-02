package com.example.udmath.presentation.MaterialApoyo

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
import androidx.compose.ui.res.painterResource

// import androidx.navigation.NavController // ← Descomenta esto si usas Navigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// fun MaterialInteresanteScreen(navController: NavController) { // ← cuando uses navegación
fun MaterialApoyoScreen() {
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
                .padding(16.dp)
        ) {


            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
            }
        }
    }
}

@Composable
fun TarjetaInteresante(
    titulo: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = titulo,
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp)
            )


            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = titulo,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF004AAD),
                textAlign = TextAlign.Center
            )
        }
    }
}