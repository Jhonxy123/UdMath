package com.example.udmath.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.udmath.presentation.components.BubbleIcon

data class AdminBottomBarItem(
    val destination: Any,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun AdminBottomBar(navController: NavHostController) {
    val items = listOf(
        AdminBottomBarItem(HomeAdminTab, "Inicio", Icons.Filled.Home),
        AdminBottomBarItem(BaseDatosTab, "Datos", Icons.Filled.People),
        AdminBottomBarItem(EditContenidoTab, "Contenido", Icons.Filled.MenuBook),
        AdminBottomBarItem(GraficasTab, "Graficas", Icons.Filled.People),
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val itemRoute = item.destination::class.qualifiedName
            val selected = currentRoute == itemRoute

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.destination) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    BubbleIcon(
                        icon = item.icon,
                        contentDescription = item.label,
                        selected = selected
                    )
                },
                label = { Text(item.label) }
            )
        }
    }
}
