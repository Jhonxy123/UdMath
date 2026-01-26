package com.example.udmath.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BubbleIcon(
    icon: ImageVector,
    contentDescription: String?,
    selected: Boolean
) {
    if (selected) {
        Box(
            modifier = Modifier
                .size(44.dp)               // tamaño del círculo
                .clip(CircleShape)
                .background(Color.White)   // fondo blanco
                .padding(6.dp),            // espacio interno
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color(0xFF1976D2),  // azul tipo "check"
                modifier = Modifier.size(26.dp)
            )
        }
    } else {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.Gray,            // no seleccionado
            modifier = Modifier.size(24.dp)
        )
    }
}
