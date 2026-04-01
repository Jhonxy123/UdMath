package com.example.udmath.presentation.welcome

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udmath.R

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel,
    navigateToRegister: () -> Unit,
    navigationToLogin: () -> Unit,
    navigateToHome: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val activity = LocalContext.current as? Activity

    LaunchedEffect(Unit) {
        viewModel.checkActiveSession(
            onNavigateToHome = navigateToHome
        )
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF3980C2), Color(0xFF184998))
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
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Bienvenido",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            if (state.isLoading) {
                Spacer(modifier = Modifier.height(12.dp))
                CircularProgressIndicator(color = Color.White)
            }

            state.error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = it, color = Color.Red)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WelcomeButton(
                    text = "Registrarse",
                    onClick = navigateToRegister,
                    enabled = !state.isLoading
                )

                WelcomeButton(
                    text = "Iniciar Sesión",
                    onClick = navigationToLogin,
                    enabled = !state.isLoading
                )

                WelcomeButton(
                    text = "Microsoft",
                    onClick = {
                        if (activity != null) {
                            viewModel.loginWithMicrosoft(activity) {
                                navigateToHome()
                            }
                        } else {
                            viewModel.setError("No se pudo obtener Activity")
                        }
                    },
                    enabled = !state.isLoading,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.microsoft),
                            contentDescription = "Microsoft",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified
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
    enabled: Boolean = true,
    leadingIcon: (@Composable (() -> Unit))? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled,
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
            containerColor = Color(0x33FFFFFF),
            contentColor = Color.White,
            disabledContainerColor = Color(0x22FFFFFF),
            disabledContentColor = Color.White
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
            Text(text = text, fontWeight = FontWeight.SemiBold)
        }
    }
}