package com.example.udmath.presentation.MaterialInteresante

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.unit.sp
import com.example.udmath.R
import com.example.udmath.presentation.components.TopBar
import com.example.udmath.presentation.components.TopBarStd
import com.example.udmath.ui.theme.white
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MaterialInteresanteScreen() {

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
                TopBarStd("Material Interesante")
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(blueGradient)   // ✅ pinta toda la pantalla
                .padding(innerPadding)      // ✅ el contenido respeta topBar/bottomBar
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()

            ) {
                Row() {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(180.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(white)
                            .padding(10.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.articulo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(110.dp),

                        )

                        Text(
                            text = "Artículos",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(10.dp),
                            color = Color.Black
                        )

                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(180.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(white)
                            .padding(10.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.programacion),
                            contentDescription = "Logo",
                            modifier = Modifier.size(110.dp),

                            )

                        Text(
                            text = "Programación",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(10.dp),
                            color = Color.Black
                        )

                    }
                }
                Row() {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(180.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(white)
                            .padding(10.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.curiosos),
                            contentDescription = "Logo",
                            modifier = Modifier.size(110.dp),

                            )

                        Text(
                            text = "Datos curiosos",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(10.dp),
                            color = Color.Black
                        )

                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(180.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(white)
                            .padding(10.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.audiovisual),
                            contentDescription = "Logo",
                            modifier = Modifier.size(90.dp),

                            )

                        Text(
                            text = "Material audiovisual",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(10.dp),
                            color = Color.Black
                        )

                    }
                }
            }


        }
    }

}