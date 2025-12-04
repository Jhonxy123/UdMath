package com.example.udmath.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ButtonDefaults.buttonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.Navigation

@Composable
fun NavigationDrawerButton(
    texto: String,
    iconoIzquierda: ImageVector,
    iconoDerecha: ImageVector = Icons.Default.KeyboardArrowRight,
    colorFondo: Color = Color(0xFF184998),
    colorTexto: Color = Color.White,
    onclick: () -> Unit
){
    Button(
        onClick = onclick,
        modifier = Modifier.fillMaxWidth(0.9f)// Ocupa el 90% del ancho de la pantalla
            .height(50.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp)), //Sombra y bordes redondeados
        shape = RoundedCornerShape(12.dp), // Bordes redondeados
        colors = buttonColors(
            containerColor = colorFondo, // Color de fondo del botón
            contentColor = Color.White
        ),
        elevation = buttonElevation( //Elevar el boton
            defaultElevation = 8.dp,
            pressedElevation = 0.dp
        )
    ) {
        //CONTENIDO DEL BOTON
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            //CONTENIDO DE LA FILA
            Icon(
                imageVector = iconoIzquierda,
                contentDescription = "Editar",
                tint = Color.White
            )

            Text(
                text = texto,
                color = colorTexto,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Icon(
                imageVector = iconoDerecha, // ➡️ Flecha a la derecha
                contentDescription = "Ir a perfil",
                modifier = Modifier.size(28.dp)
            )
        }

    }
}
