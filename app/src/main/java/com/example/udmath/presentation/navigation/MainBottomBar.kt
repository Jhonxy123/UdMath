package com.example.udmath.presentation.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController

@Composable
fun MainBottomBar(navController: NavHostController) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    val items = listOf(
        HomeTab to "Inicio",
        RecomendacionesTab to "Rec",
        MaterialTab to "Material",
        MaterialIntTab to "Mat-Int",
        RetosTab to "Retos"
    )

    NavigationBar {
        items.forEachIndexed { index, (destination, label) ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    navController.navigate(destination) {
                        //  esto evita el recorrido hacia atrás por tabs
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { /* luego ponemos iconos */ },
                label = { Text(label) }
            )
        }
    }
}
