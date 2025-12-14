package com.example.udmath.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.udmath.presentation.auth.login.LoginScreen
import com.example.udmath.presentation.auth.login.LoginViewModel
import com.example.udmath.presentation.auth.menu.MenuScreen
import com.example.udmath.presentation.auth.menu.MenuViewModel
import com.example.udmath.presentation.auth.register.RegisterScreen
import com.example.udmath.presentation.auth.register.RegisterViewModel
import com.example.udmath.presentation.home.HomeScreen
import com.example.udmath.presentation.home.HomeViewModel
import com.example.udmath.presentation.welcome.WelcomeScreen
import com.example.udmath.presentation.welcome.WelcomeViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun Navigation(
    auth: FirebaseAuth
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Welcome//Login
    ) {

        composable<Welcome> {
            val vm: WelcomeViewModel = hiltViewModel()

            WelcomeScreen(
                viewModel = vm,
                navigateToRegister = { navController.navigate(Register) },
                navigationToLogin = { navController.navigate(Login) },
                navigateToHome = { navController.navigate(Home) }
            )
        }

        composable<Login> {
            val viewModel: LoginViewModel = hiltViewModel()

            LoginScreen(
                viewModel = viewModel,
                auth = auth,
                navigateToRegister = { navController.navigate(Register) },
                navigateToMenu = { navController.navigate(Home) },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<Register> {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Home) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }


        composable<Menu> {
            val viewModel: MenuViewModel = hiltViewModel()
            MenuScreen(viewModel)
        }

        composable<Home> {
            val viewModel: HomeViewModel = hiltViewModel()

            HomeScreen(
                viewModel = viewModel,
                onLoggedOut = {
                    // Cuando el ViewModel emita LoggedOut, navegamos al Login
                    navController.navigate(Login) {
                        // Limpiamos Home del backstack para que no pueda volver atrás
                        popUpTo(Home) { inclusive = true }
                    }
                }
            )
        }
    }
}
