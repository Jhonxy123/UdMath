package com.example.udmath.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.udmath.presentation.components.BottomBar
import com.example.udmath.presentation.components.NavigationDrawer
import com.example.udmath.presentation.components.TopBar
import com.example.udmath.presentation.home.interesante.MaterialInteresanteScreen
import com.example.udmath.presentation.home.main.MainScreen
import com.example.udmath.presentation.home.materiales.MaterialApoyoScreen
import com.example.udmath.presentation.home.recomendaciones.RecomendacionesScreen
import com.example.udmath.presentation.home.retos.RetosScreen
import com.example.udmath.presentation.home.retos.game._48.GameScreen
import com.example.udmath.presentation.home.retos.game.sudoku.SudokuScreen
import kotlinx.coroutines.launch



@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: HomeRoutes.Main.route
    val openDrawer by viewModel.drawerState.collectAsState()

    LaunchedEffect(openDrawer) {
        if (openDrawer && !drawerState.isOpen) {
            drawerState.open()
            viewModel.onDrawerClose()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawer()
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                // Agregar padding de la barra de estado para que no se desborde
                Box(modifier = Modifier.statusBarsPadding()) {
                    TopBar(
                        onDrawerClicked = {
                            viewModel.onDrawerOpen()
                        }
                    )
                }
            },
            bottomBar = {
                BottomBar(
                    currentRoute = currentRoute,
                    onItemSelected = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo(HomeRoutes.Main.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            // Aplica correctamente el padding del scaffold
            NavHost(
                navController = navController,
                startDestination = HomeRoutes.Main.route,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .navigationBarsPadding() // evitar que contenido se oculte tras la barra inferior
            ) {
                composable(HomeRoutes.Main.route) {
                    MainScreen()
                }

                composable(HomeRoutes.Recomendaciones.route) {
                    RecomendacionesScreen()
                }

                composable(HomeRoutes.Materiales.route) {
                    MaterialApoyoScreen()
                }

                composable(HomeRoutes.Retos.route) {
                    RetosScreen(navController = navController)
                }

                composable(HomeRoutes.Interesante.route) {
                    MaterialInteresanteScreen()
                }

                composable("sudoku") {
                    SudokuScreen()
                }

                composable("_48") {
                    GameScreen()
                }

            }
        }
    }
}


