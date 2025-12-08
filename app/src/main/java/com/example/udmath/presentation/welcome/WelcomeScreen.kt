package com.example.udmath.presentation.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.udmath.R

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = viewModel(),
    navigateToRegister: () -> Unit,
    navigationToLogin: () -> Unit,
    navigationToMicrosoft: () -> Unit
) {
    // Degradado de fondo: #3980C2 -> #184998
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3980C2),
            Color(0xFF184998)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Parte superior (puedes dejarla vacía si no quieres logo)
            Spacer(modifier = Modifier.height(32.dp))

            // Texto "Bienvenido"
            Text(
                text = "Bienvenido",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            // Botones en la parte inferior
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WelcomeButton(
                    text = "Registrarse",
                    onClick = navigateToRegister
                )

                WelcomeButton(
                    text = "Iniciar Sesión",
                    onClick = navigationToLogin
                )

                WelcomeButton(
                    text = "Microsoft",
                    onClick = navigationToMicrosoft,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.microsoft), // tu recurso
                            contentDescription = "Microsoft",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified     // Para que mantenga sus colores originales
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun WelcomeButton(
    text: String,
    onClick: () -> Unit,
    leadingIcon: (@Composable (() -> Unit))? = null
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(18.dp),
                clip = false
            ),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0x33FFFFFF), // Fondo clarito translúcido
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}