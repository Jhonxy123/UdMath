/*package com.example.udmath.presentation.MaterialApoyo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.udmath.R
import com.example.udmath.presentation.components.TopBarStd


@Composable
@Preview
fun MaterialApoyoScreen2() {

    TopBarStd("Material Apoyo")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2F73B8))
    ) {

        // despues dibujamos la curva que conecta las burbujas
        // size.width y size.height son los tamaños reales del canvas en píxeles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            //arcRect define el círculo “invisible” donde vive el arco
            val arcRect = Rect(
                left = w * 0.30f,
                top = h * 0.20f,
                right = w * 1.70f,
                bottom = h * 0.80f
            )

            drawArc(
                color = Color.White,
                startAngle = 90f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = arcRect.topLeft,
                size = arcRect.size,
                style = Stroke(
                    width = 16f,
                    cap = StrokeCap.Round
                )
            )

        }

        // creamos las diferentes burbujas que van a contener los iconos de nuestro material
        //ofset(x,y), maneja las posiciones de las burbujas
        Bubble(
            image = R.drawable.aplicaciones,
            modifier = Modifier.offset(x = 300.dp, y = 147.dp)
        )
        Bubble(
            image = R.drawable.libros,
            modifier = Modifier.offset(x = 190.dp, y = 200.dp)
        )
        Bubble(
            image = R.drawable.paginasapoyo,
            modifier = Modifier.offset(x = 100.dp, y = 320.dp)
        )
        Bubble(
            image = R.drawable.tutoriasud,
            modifier = Modifier.offset(x = 100.dp, y = 470.dp)
        )
        Bubble(
            image = R.drawable.videos,
            modifier = Modifier.offset(x = 190.dp, y = 590.dp)
        )
        Bubble(
            image = R.drawable.articulo,
            modifier = Modifier.offset(x = 300.dp, y = 640.dp)
        )

    }
}


@Composable
fun Bubble(
    image: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(Color(0xFFF2F2F2))
            .border(2.dp, Color.Black, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Logo",
            modifier = Modifier.size(40.dp)
        )
    }
}
*/