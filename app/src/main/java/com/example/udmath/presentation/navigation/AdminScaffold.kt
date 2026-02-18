package com.example.udmath.presentation.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.udmath.presentation.admin.Home.HomeAdminScreen
import com.example.udmath.presentation.admin.AdminViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AdminScaffold(
    auth: FirebaseAuth,
    onLogout: () -> Unit,
    navigateToProfile: () -> Unit = {}
) {

    val tabNavController = rememberNavController()

    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val activity = LocalContext.current as? Activity

    val isOnHome = currentRoute == HomeAdminTab::class.qualifiedName

    // 🔙 comportamiento del botón físico back
    BackHandler(enabled = true) {
        if (!isOnHome) {
            tabNavController.navigate(HomeAdminTab) {
                popUpTo(tabNavController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        } else {
            activity?.finish()
        }
    }

    Scaffold(
        bottomBar = {
            AdminBottomBar(tabNavController)
        }
    ) { padding ->

        NavHost(
            navController = tabNavController,
            startDestination = HomeAdminTab,
            modifier = Modifier.padding(padding)
        ) {

            // 🏠 HOME ADMIN
            composable<HomeAdminTab> {

                val vm: AdminViewModel = hiltViewModel()

                HomeAdminScreen(
                    viewModel = vm,
                    onLoggedOut = onLogout,
                    navigateToProfile = navigateToProfile
                )
            }

            // 👥 BASE DE DATOS / USUARIOS
            composable<BaseDatosTab> {

                // TODO: tu pantalla de gestión de usuarios admin
                // AdminUsersScreen()

            }

            // ✏️ EDITAR CONTENIDO
            composable<EditContenidoTab> {

                // TODO: tu pantalla para editar contenido exclusivo admin
                // AdminEditContentScreen()

            }

            // 📊 GRÁFICAS
            composable<GraficasTab> {

                // TODO: tu pantalla de gráficas
                // AdminGraficasScreen()

            }
        }
    }
}