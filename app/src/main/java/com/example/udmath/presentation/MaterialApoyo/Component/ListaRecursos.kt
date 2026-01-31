package com.example.udmath.presentation.MaterialApoyo.Component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.udmath.domain.model.Recurso
import com.example.udmath.presentation.components.TopBarback

@Composable
fun ListaRecursosScreen(
    tituloTopBar: String,
    recursos: List<Recurso>,
    navigateBack: () -> Unit,
    onRecursoClick: (Recurso) -> Unit = {}
){
    val blueGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3980C2),
            Color(0xFF184998)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(blueGradient)
    ) {
        TopBarback(tituloTopBar, navigateBack)

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(recursos, key = { it.titulo }) { item ->
                RecursoCard(
                    recurso = item,
                    onClick = { onRecursoClick(item) }
                )
            }
        }
    }
}
