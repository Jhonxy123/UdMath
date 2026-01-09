package com.example.udmath.presentation.auth.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udmath.R
import com.example.udmath.presentation.components.CodeText
import com.example.udmath.presentation.components.PasswordText
import com.example.udmath.presentation.components.TextCampName
import com.example.udmath.presentation.components.blueGradient
import com.example.udmath.presentation.components.emailText
import com.example.udmath.ui.theme.Blue
import com.example.udmath.ui.theme.white

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(blueGradient)
    ) {

        Column(

            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .background(blueGradient)
                .padding(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier.size(130.dp),
                painter = painterResource(R.drawable.udmath),
                contentDescription = "Logo"
            )

            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "Registrate",
                fontWeight = FontWeight.Bold,
                color = white
            )
        }

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(Color.White)
                .padding(25.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            state.errorMessage?.let {
                Text(text = it, color = Color.Red)
            }

            TextCampName(
                cadena = state.name,
                onTextChanged = { viewModel.onNameChange(it) }
            )

            CodeText(
                code = state.code,
                onTextChanged = { viewModel.onCodeChange(it) }
            )

            emailText(
                email = state.email,
                onTextChanged = { viewModel.onEmailChange(it) }
            )

            PasswordText(
                password = state.password,
                onTextChanged = { viewModel.onPasswordChange(it) },
                showPassword = state.showPassword,
                onToggleVisibility = { viewModel.togglePasswordVisibility() }
            )

            PasswordText(
                password = state.confirmPassword,
                onTextChanged = { viewModel.onConfirmPasswordChange(it) },
                showPassword = state.showConfirmPassword,
                onToggleVisibility = { viewModel.toggleConfirmPasswordVisibility() }
            )

            Spacer(modifier = Modifier.height(18.dp))

            /* =======================
       🔘 BOTÓN INGRESAR (Mockup)
       ======================= */
            Button(
                onClick = { viewModel.register(onRegisterSuccess) },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E2E2)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF184998),
                    disabledContainerColor = Color(0xFFF5F5F5),
                    disabledContentColor = Color(0xFF184998).copy(alpha = 0.5f)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 2.dp,     // 👈 se “hunde” al presionar
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    text = if (state.isLoading) "Cargando..." else "Crear cuenta",
                    fontSize = 16.sp,
                    color = Color(0xFF184998)
                )
            }
            
            TextButton(
                onClick = navigateBack,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Volver",
                    color = Blue,      // 👈 mismo azul
                    fontSize = 14.sp
                )
            }


        }


    }
}
