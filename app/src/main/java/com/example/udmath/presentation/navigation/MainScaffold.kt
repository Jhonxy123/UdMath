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
import com.example.udmath.presentation.MaterialApoyo.MaterialApoyoScreen
import com.example.udmath.presentation.MaterialInteresante.MaterialInteresanteScreen
import com.example.udmath.presentation.Recomendaciones.RecomendacionesScreen
import com.example.udmath.presentation.Retos.RetosScreen
import com.example.udmath.presentation.auth.register.RegisterScreen
import com.example.udmath.presentation.auth.register.RegisterViewModel
import com.example.udmath.presentation.home.HomeScreen
import com.example.udmath.presentation.home.HomeViewModel
import com.example.udmath.presentation.profile.PerfilScreen
import com.example.udmath.presentation.profile.PerfilViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainScaffold(
    auth: FirebaseAuth,
    onLogout: () -> Unit
) {
    val tabNavController = rememberNavController()
    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Lee el Activity aquí (zona @Composable)
    val activity = LocalContext.current as? Activity

    val isOnHome = currentRoute?.contains("HomeTab") == true

    BackHandler(enabled = true) {
        if (!isOnHome) {
            tabNavController.navigate(HomeTab) {
                popUpTo(tabNavController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        } else {
            //  salir de la app
            activity?.finish()
        }
    }

    Scaffold(
        bottomBar = { MainBottomBar(tabNavController) }
    ) { padding ->

        NavHost(
            navController = tabNavController,
            startDestination = HomeTab,
            modifier = Modifier.padding(padding)
        ) {
            composable<HomeTab> {
                val vm: HomeViewModel = hiltViewModel()
                HomeScreen(
                    viewModel = vm,
                    onLoggedOut = onLogout,
                    navigateToProfile = { tabNavController.navigate(PerfilTab) }
                )
            }

            composable<PerfilTab> {
                val vm: PerfilViewModel = hiltViewModel()
                PerfilScreen(
                    viewModel = vm,
                    navigateBack = { tabNavController.popBackStack() }
                )
            }

            composable<MaterialTab> {
                MaterialApoyoScreen()
            }

            composable<MaterialIntTab> {
                MaterialInteresanteScreen()
            }

            composable<RecomendacionesTab> {
                RecomendacionesScreen()
            }

            composable<RetosTab> {
                RetosScreen()
            }

        }
    }
}
