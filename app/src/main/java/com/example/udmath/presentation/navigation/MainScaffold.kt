package com.example.udmath.presentation.navigation

import MainBottomBar
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
import com.example.udmath.presentation.Algebra.AlgebraScreen
import com.example.udmath.presentation.Aritmetica.AritmeticaScreen
import com.example.udmath.presentation.Funciones.FuncionesScreen
import com.example.udmath.presentation.MaterialApoyo.Aplicaciones.AplicacionesScreen
import com.example.udmath.presentation.MaterialApoyo.Component.RecursosScreen
import com.example.udmath.presentation.MaterialApoyo.Libros.LibrosScreen
import com.example.udmath.presentation.MaterialApoyo.MaterialApoyoScreen
import com.example.udmath.presentation.MaterialInteresante.MaterialInteresanteScreen
import com.example.udmath.presentation.Recomendaciones.RecomendacionesScreen
import com.example.udmath.presentation.Retos.RetosScreen
import com.example.udmath.presentation.Retos._48.GameScreen
import com.example.udmath.presentation.Retos.sudoku.SudokuScreen
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

    val isOnHome = currentRoute == HomeTab::class.qualifiedName

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
                MaterialApoyoScreen(
                    navigatelibros = { tabNavController.navigate(LibrosTab) },
                    navigateaplicaciones = { tabNavController.navigate(AplicacionesTab) },
                )
            }

            composable<MaterialIntTab> {
                MaterialInteresanteScreen()
            }

            composable<RecomendacionesTab> {
                RecomendacionesScreen(
                    onAritmetica = { tabNavController.navigate(AritmeticaTab) },
                    onAlgebra = { tabNavController.navigate(AlgebraTab) },
                    onFunciones = { tabNavController.navigate(FuncionesTab) }
                )
            }

            composable<RetosTab> {
                RetosScreen(
                    onSudoku = {tabNavController.navigate(SudokuTab)},
                    on2048 = {tabNavController.navigate(Game2048Tab)}
                )

            }

            composable<SudokuTab> {
                SudokuScreen(
                    navigateBack = { tabNavController.popBackStack() }
                )
            }

            composable<Game2048Tab> {
                GameScreen(
                    navigateBack = { tabNavController.popBackStack() }
                )
            }

            composable<AritmeticaTab> {
                AritmeticaScreen(
                    navigateBack = { tabNavController.popBackStack() }
                )
            }

            composable<AlgebraTab> {
                AlgebraScreen(
                    navigateBack = { tabNavController.popBackStack() }
                )
            }

            composable<FuncionesTab> {
                FuncionesScreen(
                    navigateBack = { tabNavController.popBackStack() }
                )
            }

            composable<LibrosTab> {
                LibrosScreen(
                    navigateBack = { tabNavController.popBackStack() }
                )
            }

            composable<AplicacionesTab> {
                AplicacionesScreen(
                    navigateBack = { tabNavController.popBackStack() }
                )
            }



        }
    }
}
