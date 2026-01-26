package com.example.udmath.presentation.Funciones

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.udmath.presentation.components.TopBarStd
import com.example.udmath.presentation.components.TopBarback

@Composable
fun FuncionesScreen(
    navigateBack: () -> Unit
){

    Scaffold(

        topBar = { TopBarback("Funciones", navigateBack = {navigateBack()}) }

    ) { padding ->

        Column(
            Modifier.padding(padding)
                .fillMaxSize()
        ){
            Text("Contenido de Funciones")
        }

    }

}