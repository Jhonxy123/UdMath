package com.example.udmath.presentation.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.example.udmath.R
import com.example.udmath.presentation.components.CodeText
import com.example.udmath.presentation.components.TextCampName
import com.example.udmath.presentation.components.blueGradient
import com.example.udmath.presentation.components.emailText
import com.example.udmath.presentation.components.passwordText
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

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(Color.White)
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            TextCampName(
                cadena = state.name,
                onTextChanged = { viewModel.onNameChange(it) }
            )

            // 🔹 Código
            CodeText(
                code = state.code,
                onTextChanged = { viewModel.onCodeChange(it) }
            )

            // 🔹 Correo
            emailText(
                email = state.email,
                onTextChanged = { viewModel.onEmailChange(it) }
            )

            // 🔹 Contraseña
            passwordText(
                password = state.password,
                onTextChanged = { viewModel.onPasswordChange(it) }
            )

            // Aquí iría el botón de registrar
            Button(
                onClick = { viewModel.register(onRegisterSuccess) },
                enabled = !state.isLoading   // DESACTIVADO mientras está cargando
            ) {
                Text("Crear cuenta")
            }


            // Botón atrás opcional
            TextButton(onClick = navigateBack) {
                Text("Volver")
            }

            // Puedes mostrar errores
            state.errorMessage?.let {
                Text(text = it, color = Color.Red)
            }

        }


    }
}
