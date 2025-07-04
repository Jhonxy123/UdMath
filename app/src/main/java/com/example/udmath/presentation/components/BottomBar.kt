package com.example.udmath.presentation.components

import com.example.udmath.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.udmath.presentation.home.HomeRoutes


@Composable
fun BottomBar(
    currentRoute: String,
    onItemSelected: (String) -> Unit
) {
    val items = listOf(
        HomeRoutes.Main,
        HomeRoutes.Recomendaciones,
        HomeRoutes.Materiales,
        HomeRoutes.Interesante,
        HomeRoutes.Retos
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEach { route ->
            val isSelected = currentRoute == route.route

            Surface(
                modifier = Modifier
                    .size(58.dp)
                    .clickable { onItemSelected(route.route) },
                shape = CircleShape,
                tonalElevation = 8.dp,
                color = if (isSelected) Color(0xFF0D47A1) else Color.White,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = when (route) {
                            is HomeRoutes.Main -> R.drawable.ic_home
                            is HomeRoutes.Recomendaciones -> R.drawable.ic_check
                            is HomeRoutes.Materiales -> R.drawable.ic_user
                            is HomeRoutes.Interesante -> R.drawable.ic_idea
                            is HomeRoutes.Retos -> R.drawable.ic_target
                        }),
                        contentDescription = null,
                        tint = if (isSelected) Color.White else Color(0xFF0D47A1),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}
