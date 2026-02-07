package com.example.udmath.presentation.MaterialApoyo

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import com.example.udmath.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.udmath.presentation.components.NavigationDrawer
import com.example.udmath.presentation.components.TopBar
import com.example.udmath.presentation.components.TopBarStd
import com.example.udmath.presentation.components.blueGradient
import com.example.udmath.ui.theme.white
import kotlinx.coroutines.launch

// import androidx.navigation.NavController // ← Descomenta esto si usas Navigation


data class BubbleItem(
    val image: Int,
    val text: String,
    val pos: Pair<Float, Float>,
    val onClick: () -> Unit
)



@Composable
//@Preview
fun MaterialApoyoScreen(
    navigatelibros: () -> Unit = {},
    navigateaplicaciones: () -> Unit = {},
    navigateVideos: () -> Unit = {}
) {
    val blueGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF3980C2), Color(0xFF184998))
    )
    val context = LocalContext.current


    Scaffold(
        topBar = { TopBarStd("Material de Apoyo") },
        bottomBar = { /* tu BottomBar */ }
    ) { innerPadding ->

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(blueGradient)   //  primero
                .padding(innerPadding)
        ) {
            val bubbleSize = 70.dp

            // maxWidth / maxHeight están en dp (ya con padding aplicado)
            val w = maxWidth
            val h = maxHeight

            // --- Curva ---
            Canvas(modifier = Modifier.fillMaxSize()) {
                val wPx = size.width
                val hPx = size.height

                val arcRect = Rect(
                    left = wPx * 0.30f,
                    top = hPx * 0.08f,
                    right = wPx * 1.70f,
                    bottom = hPx * 0.90f
                )

                drawArc(
                    color = Color.White,
                    startAngle = 90f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = arcRect.topLeft,
                    size = arcRect.size,
                    style = Stroke(width = 16f, cap = StrokeCap.Round)
                )
            }

            // --- Burbujas (posiciones RELATIVAS) ---
            // x e y son fracciones del ancho/alto disponible (0f..1f)
            val bubbles = listOf(
                BubbleItem(R.drawable.aplicaciones, "Aplicaciones", 0.58f to 0.08f) { navigateaplicaciones()  },
                BubbleItem(R.drawable.libros, "Libros", 0.34f to 0.20f) { navigatelibros() },
                BubbleItem(R.drawable.paginasapoyo, "Páginas\nde apoyo", 0.11f to 0.45f) { /* ... */ },
                BubbleItem(R.drawable.tutoriasud, "Tutorías UD", 0.20f to 0.72f) { abrir_link(context,"https://bibliotecas.udistrital.edu.co/formulario/tutorias_matematicas" ) },
                BubbleItem(R.drawable.videos, "Videos", 0.68f to 0.89f) { navigateVideos() }
            )


            bubbles.forEach { item ->
                val (xf, yf) = item.pos
                Bubble(
                    image = item.image,
                    text = item.text,
                    modifier = Modifier.absoluteOffset(
                        x = (w * xf) - (bubbleSize / 2),
                        y = (h * yf) - (bubbleSize / 2)
                    ),
                    onClick = item.onClick
                )
            }
        }
    }
}

fun abrir_link(context: Context, link: String) {
    val safeLink =
        if (link.startsWith("http://") || link.startsWith("https://"))
            link
        else
            "https://$link"

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(safeLink))
    context.startActivity(intent)
}



@Composable
fun Bubble(
    image: Int,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onClick() },   // ✅ aquí
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            softWrap = true,
            maxLines = 2,
            modifier = Modifier.widthIn(max = 120.dp)
        )

        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(Color(0xFFF2F2F2))
                .border(2.dp, Color.Black, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}



