package com.example.udmath.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.udmath.presentation.auth.login.LoginDestination
import com.example.udmath.presentation.auth.login.LoginScreen
import com.example.udmath.presentation.auth.login.LoginViewModel
import com.example.udmath.presentation.auth.register.RegisterScreen
import com.example.udmath.presentation.auth.register.RegisterViewModel
import com.example.udmath.presentation.auth.verify_email.VerifyEmailScreen
import com.example.udmath.presentation.auth.verify_email.VerifyEmailViewModel
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
        startDestination = Welcome
    ) {

        composable<Welcome> {
            val vm: WelcomeViewModel = hiltViewModel()

            WelcomeScreen(
                viewModel = vm,
                navigateToRegister = { navController.navigate(Register) },
                navigationToLogin = { navController.navigate(Login) },
                navigateToHome = {
                    navController.navigate(Main) {
                        popUpTo(Welcome) { inclusive = true }
                    }
                }
            )
        }

        composable<Login> {
            val viewModel: LoginViewModel = hiltViewModel()

            LoginScreen(
                viewModel = viewModel,
                navigateToMenu = {
                    navController.navigate(Main) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                navigateToAdmin = {
                    navController.navigate(Admin) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                navigateToVerifyEmail = { email ->
                    navController.navigate(VerifyEmail(email)) {
                        popUpTo(Login) { inclusive = false }
                    }
                },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<Register> {
            val viewModel: RegisterViewModel = hiltViewModel()

            RegisterScreen(
                viewModel = viewModel,
                navigateBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Login) {
                        popUpTo(Register) { inclusive = true }
                    }
                }
            )
        }

        composable<VerifyEmail> { backStackEntry ->
            val route = backStackEntry.toRoute<VerifyEmail>()
            val viewModel: VerifyEmailViewModel = hiltViewModel()

            VerifyEmailScreen(
                email = route.email,
                viewModel = viewModel,
                onVerified = {
                    navController.navigate(Main) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<Main> {
            MainScaffold(
                auth = auth,
                onLogout = {
                    navController.navigate(Welcome) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable<Admin> {
            AdminScaffold(
                auth = auth,
                onLogout = {
                    navController.navigate(Welcome) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}