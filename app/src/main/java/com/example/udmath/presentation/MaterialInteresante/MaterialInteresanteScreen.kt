package com.example.udmath.presentation.MaterialInteresante


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.udmath.R
import com.example.udmath.presentation.components.TopBarStd
import com.example.udmath.presentation.MaterialInteresante.Components.MaterialCard



data class MaterialItem(
    val title: String,
    val iconRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MaterialInteresanteScreen() {
    val blueGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF3980C2), Color(0xFF184998))
    )

    val itemsList = listOf(
        MaterialItem("Artículos", R.drawable.articulo),
        MaterialItem("Programación", R.drawable.programacion),
        MaterialItem("Datos curiosos", R.drawable.curiosos),
        MaterialItem("Material audiovisual", R.drawable.audiovisual)
    )

    Scaffold(
        topBar = { TopBarStd("Material Interesante") }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(blueGradient)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp), // se adapta
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(itemsList) { item ->
                    MaterialCard(
                        title = item.title,
                        iconRes = item.iconRes
                    )
                }
            }
        }
    }
}

