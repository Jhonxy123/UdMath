package com.example.udmath.presentation.Recomendaciones.Algebra

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udmath.presentation.components.TopBarStd
import com.example.udmath.presentation.components.TopBarback
import com.example.udmath.ui.theme.white

@Composable
fun AlgebraScreen(
    navigateBack: () -> Unit
){

    Scaffold(

        topBar = { TopBarback("Algebra", navigateBack = {navigateBack()}) }

    ) { padding ->

        Column(
            Modifier.padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Contenido de Algebra")
        }

    }

}

