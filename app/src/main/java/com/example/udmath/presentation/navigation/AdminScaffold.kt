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
import com.example.udmath.presentation.admin.BaseDatos.BdScreen
import com.example.udmath.presentation.admin.Home.HomeAdminScreen
import com.example.udmath.presentation.admin.AdminViewModel
import com.example.udmath.presentation.admin.BaseDatos.BdViewModel
import com.example.udmath.presentation.admin.BaseDatos.EditUserScreen
import com.example.udmath.presentation.admin.BaseDatos.EditUserViewModel
import com.example.udmath.presentation.admin.EditarContenido.EditarContenidoScreen
import com.example.udmath.presentation.admin.Graficas.GraficasScreen
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

    BackHandler(enabled = true) {
        if (!isOnHome) {
            tabNavController.navigate(HomeAdminTab) {
                popUpTo(tabNavController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        } else {
            activity?.finish()
        }
    }

    Scaffold(
        bottomBar = { AdminBottomBar(tabNavController) }
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
                val vm: BdViewModel = hiltViewModel()
                BdScreen(
                    viewModel = vm,
                    navigateToEdit = { userId ->
                        tabNavController.navigate(EditUserRoute(userId))
                    }
                )
            }

            // ✏️ EDITAR USUARIO (nueva pantalla)
            composable<EditUserRoute> { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                val vm: EditUserViewModel = hiltViewModel()
                EditUserScreen(
                    userId = userId,
                    onBack = { tabNavController.popBackStack() }
                )
            }

            // ✏️ EDITAR CONTENIDO
            composable<EditContenidoTab> {
                EditarContenidoScreen(
                    onRecomendaciones = {
                        // tabNavController.navigate(RutaRecomendaciones)  // si tienes otra pantalla
                    },
                    onMaterialApoyo = {
                        // tabNavController.navigate(RutaMaterialApoyo)
                    },
                    onMaterialInteresante = {
                        // tabNavController.navigate(RutaMaterialInteresante)
                    }
                )
            }

            // 📊 GRÁFICAS
            composable<GraficasTab> {
                GraficasScreen(
                    onDownloadPdf = {
                        // TODO: aquí llamas tu lógica para generar PDF
                    }
                )
            }

        }
    }
}