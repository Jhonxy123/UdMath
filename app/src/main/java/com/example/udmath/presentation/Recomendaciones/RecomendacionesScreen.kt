package com.example.udmath.presentation.Recomendaciones

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udmath.R
import com.example.udmath.presentation.components.TopBarStd
import com.example.udmath.ui.theme.Black
import com.example.udmath.ui.theme.white


@Composable
fun RecomendacionesScreen(
    onAritmetica: () -> Unit,
    onAlgebra: () -> Unit,
    onFunciones: () -> Unit
) {
    //Definimos el gradiente de colores
    val blueGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3980C2),
            Color(0xFF184998)
        )
    )
    //Scafold es la estructura principal que le da el aspecto al screen
    Scaffold(
        topBar = {
            TopBarStd("Recomendaciones")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(blueGradient)   // pinta toda la pantalla
                .padding(innerPadding)

        // el contenido respeta topBar/bottomBar
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.End // la manda a la derecha
                ) {
                    Row(
                        modifier = Modifier
                            .width(330.dp) // tamaño de la card
                            .height(120.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .paint(
                                painter = painterResource(id = R.drawable.fondoaritmetica_1),
                                contentScale = ContentScale.Crop
                            )
                            .clickable { onAritmetica() }
                            .padding(16.dp), // padding interno
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Aritmética",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.weight(1f)) // Empuja la flecha a la derecha

                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = "Ir",
                            tint = Color.White
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.End // la manda a la derecha
                ) {
                    Row(
                        modifier = Modifier
                            .width(330.dp) // tamaño de la card
                            .height(120.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .paint(
                                painter = painterResource(id = R.drawable.fondoalgebra),
                                contentScale = ContentScale.Crop
                            )
                            .clickable { onAlgebra() }
                            .padding(16.dp), // padding interno

                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Algebra",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.weight(1f)) // Empuja la flecha a la derecha

                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = "Ir",
                            tint = Color.White
                        )
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.End // la manda a la derecha
                ) {
                    Row(
                        modifier = Modifier
                            .width(330.dp) // tamaño de la card
                            .height(120.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .paint(
                                painter = painterResource(id = R.drawable.fondofunciones_2),
                                contentScale = ContentScale.Crop
                            )
                            .clickable { onFunciones() }
                            .padding(16.dp), // padding interno
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Funciones",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.weight(1f)) // Empuja la flecha a la derecha

                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = "Ir",
                            tint = Color.White
                        )


                    }
                }

            }
            }
    }
}