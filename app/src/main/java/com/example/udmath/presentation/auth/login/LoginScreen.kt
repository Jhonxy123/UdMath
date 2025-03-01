package com.example.udmath.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.sp
import com.example.udmath.ui.theme.Blue
import com.example.udmath.ui.theme.DarkBlue
import com.example.udmath.ui.theme.white

@Composable
fun LoginScreen( navigateToRegister: () -> Unit){
    Column( modifier= Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Blue, DarkBlue))),horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "LOGIN SCREEN", fontSize = 25.sp, color = white)
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {navigateToRegister()}) {
            Text(text = "Navegar al registro")
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}