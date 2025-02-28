package com.example.udmath.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.udmath.presentation.auth.login.LoginScreen

@Composable
fun NavigationScreen(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {
        composable<Login> { LoginScreen() }
    }

}