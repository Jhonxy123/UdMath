package com.example.udmath.presentation.auth.login

import android.R.color.black
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue//
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.udmath.presentation.auth.register.emailText
import com.example.udmath.presentation.auth.register.passwordText
import com.example.udmath.presentation.components.AutoResizedText
import com.example.udmath.presentation.components.MyBanner
import com.example.udmath.ui.theme.Blue
import com.example.udmath.ui.theme.DarkBlue
import com.example.udmath.ui.theme.white
import com.example.udmath.ui.theme.Black

@Composable
fun LoginScreen(viewModel: LoginViewModel, navigateToRegister: () -> Unit, navigateToMenu: () -> Unit){

    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()

    Column( modifier= Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MyBanner(text="Bienvenido")
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "LOGIN SCREEN", fontSize = 25.sp, color = Black)
        //Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(35.dp))
        emailText(email,{viewModel.onEmailChanged(it)}) //Campo de texto para el email del usuario
        Spacer(modifier = Modifier.height(20.dp))
        passwordText(password,{viewModel.onPasswordChanged(it)}) //Campo de texto para la contraseña del usuario
        Spacer(modifier = Modifier.height(48.dp))

        Button(onClick = {navigateToMenu()}) {
            Text(text = "Sign in")
        }
        Button(onClick = {navigateToRegister()}) {
            Text(text = "Navegar al registro")
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}