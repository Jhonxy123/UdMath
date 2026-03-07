package com.example.udmath.presentation.MaterialApoyo.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.udmath.R
import com.example.udmath.domain.model.Recurso
import com.example.udmath.presentation.components.TopBarback

@Composable
fun ListaRecursosScreen(
    tituloTopBar: String,
    recursos: List<Recurso>,
    navigateBack: () -> Unit,
    itemContent: @Composable (Recurso) -> Unit
) {
    val blueGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3980C2),
            Color(0xFF184998)
        )
    )

    val (imageRes, descripcion) = when (tituloTopBar) {
        "Libros" -> Pair(
            R.drawable.libros,
            "En esta sección encontrarás material bibliográfico que te puede ayudar a estudiar matemática"
        )
        "Aplicaciones" -> Pair(
            R.drawable.aplicaciones,
            "En esta sección encontrarás aplicaciones que te puede ayudar a estudiar matemática"
        )
        "Videos" -> Pair(
            R.drawable.videos,
            "En esta sección encontrarás videos que te pueden ayudar a estudiar matemática"
        )
        "Paginas de apoyo" -> Pair(
            R.drawable.paginasapoyo,
            "En esta sección encontrarás paginas que te ayudaran a estudiar matemáticas."
        )
        "Material de apoyo" -> Pair(
            R.drawable.paginasapoyo,
            "Aquí se muestran todos los recursos del módulo Material de apoyo."
        )
        "Material interesante" -> Pair(
            R.drawable.videos,
            "Aquí se muestran todos los recursos del módulo Material interesante."
        )
        else -> Pair(null, "")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(blueGradient)
    ) {
        TopBarback(tituloTopBar, navigateBack)

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(12.dp)
                        .border(2.dp, Color(0xFF184998))
                        .padding(9.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    imageRes?.let {
                        Image(
                            painter = painterResource(id = it),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    if (descripcion.isNotBlank()) {
                        Text(
                            text = descripcion,
                            color = Color(0xFF184998),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            items(recursos, key = { it.id }) { item ->
                itemContent(item)
            }
        }
    }
}