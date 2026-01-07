package com.example.udmath.presentation.Recomendaciones

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udmath.R
import com.example.udmath.presentation.components.TopBarStd
import com.example.udmath.ui.theme.white

@Composable
fun RecomendacionesScreen() {
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
                .background(blueGradient)   // ✅ pinta toda la pantalla
                .padding(innerPadding)      // ✅ el contenido respeta topBar/bottomBar
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.End)              // ⬅️ asegura que se pegue a la derecha
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(white)
                        .padding(10.dp)
                        .height(100.dp)
                        .width(400.dp)
                    ,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.fondo),
                            contentDescription = "Fondo",
                            modifier = Modifier.fillMaxSize(), // clave
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            painter = painterResource(id = R.drawable.aplicaciones),
                            contentDescription = "Aplicaciones",
                            modifier = Modifier.size(70.dp) //  más pequeño que el fondo
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Aplicaciones",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(white)
                        .padding(10.dp).height(100.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.fondo),
                            contentDescription = "Fondo",
                            modifier = Modifier.fillMaxSize(), // clave
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            painter = painterResource(id = R.drawable.libros),
                            contentDescription = "Libros",
                            modifier = Modifier.size(55.dp) //  más pequeño que el fondo
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Libros",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(white)
                        .padding(10.dp).height(100.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.fondo),
                            contentDescription = "Fondo",
                            modifier = Modifier.fillMaxSize(), // clave
                            contentScale = ContentScale.Crop
                        )
                        Image(
                            painter = painterResource(id = R.drawable.paginasapoyo),
                            contentDescription = "Páginas apoyo",
                            modifier = Modifier.size(70.dp) //  más pequeño que el fondo
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Página de Apoyo",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }


            }
        }
    }
}