package com.example.udmath.presentation.MaterialInteresante


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.udmath.R
import com.example.udmath.presentation.components.TopBarStd


data class MaterialItem(
    val title: String,
    val iconRes: Int,
    val onClick: () -> Unit = {}
)

@Composable
fun MaterialInteresanteScreen(
    navigateArticulos: () -> Unit = {},
    navigateDatosCuriosos: () -> Unit = {},
    navigateMaterialAudiovisual: () -> Unit = {},
    navigateProgramacion: () -> Unit = {},

) {
    val blueGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF3980C2), Color(0xFF184998))
    )

    val itemsList = listOf(
        MaterialItem("Artículos", R.drawable.articulo, { navigateArticulos() }),
        MaterialItem("Programación", R.drawable.programacion,{ navigateProgramacion()}),
        MaterialItem("Datos curiosos", R.drawable.curiosos, { navigateDatosCuriosos() }),
        MaterialItem("Material audiovisual", R.drawable.audiovisual, { navigateMaterialAudiovisual() })
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
                    MaterialCardInt(
                        title = item.title,
                        iconRes = item.iconRes,
                        onClick = item.onClick
                    )
                }
            }
        }
    }
}


@Composable
fun MaterialCardInt(
    title: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = title,
            color = Color(0xFF184998),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}


