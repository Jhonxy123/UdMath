package com.example.udmath.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.udmath.presentation.auth.login.LoginScreen
import com.example.udmath.presentation.auth.login.LoginViewModel
import com.example.udmath.presentation.auth.menu.MenuViewModel
import com.example.udmath.presentation.auth.register.RegisterScreen
import com.example.udmath.presentation.auth.register.RegisterViewModel
import com.example.udmath.presentation.auth.menu.MenuScreen

@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            val viewModel: LoginViewModel = hiltViewModel()
            //Esto es un error LoginScreen(viewModel){navController.navigate(Register),navController.navigate(Menu)}//{navController.navigate(Menu)}
            LoginScreen(
                viewModel = viewModel,
                navigateToRegister = { navController.navigate(Register) },
                navigateToMenu = { navController.navigate(Menu) }
            )
        }

        composable<Register> {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(viewModel)
        }

        composable<Menu>{
            val viewModel: MenuViewModel = hiltViewModel()
            MenuScreen(viewModel)
        }

    }

}