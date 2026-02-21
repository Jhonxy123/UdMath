package com.example.udmath.presentation.admin.EditarContenido

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun EditarContenidoScreen(
    onRecomendaciones: () -> Unit = {},
    onMaterialApoyo: () -> Unit = {},
    onMaterialInteresante: () -> Unit = {}
) {
    val bg = Color(0xFFF3F3F3)
    val blue = Color(0xFF184998)

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.Center
        ) {
            ContentBigButton(
                title = "Recomendaciones",
                icon = Icons.Default.CheckCircle,
                borderColor = blue,
                textColor = blue,
                onClick = onRecomendaciones
            )

            Spacer(Modifier.height(26.dp))

            ContentBigButton(
                title = "Material de Apoyo",
                icon = Icons.Default.VolunteerActivism,
                borderColor = blue,
                textColor = blue,
                onClick = onMaterialApoyo
            )

            Spacer(Modifier.height(26.dp))

            ContentBigButton(
                title = "Material Interesante",
                icon = Icons.Default.Lightbulb,
                borderColor = blue,
                textColor = blue,
                onClick = onMaterialInteresante
            )
        }
    }
}

@Composable
private fun ContentBigButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    borderColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)

    // Contenedor sombra (para que se vea como en la imagen)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.25f),
                spotColor = Color.Black.copy(alpha = 0.25f)
            )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clickable(onClick = onClick),
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = textColor,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(Modifier.width(12.dp))

                // Texto centrado (mantenemos simetría con un spacer al final)
                Text(
                    text = title,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )

                // Spacer para “equilibrar” el centrado del texto (visual)
                Spacer(Modifier.width(28.dp))
            }
        }
    }
}
