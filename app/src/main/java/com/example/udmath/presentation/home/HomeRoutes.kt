package com.example.udmath.presentation.home

sealed class HomeRoutes(val route: String) {
    object Main : HomeRoutes("home/main")
    object Recomendaciones : HomeRoutes("home/recomendaciones")
    object Materiales : HomeRoutes("home/materiales")
    object Interesante : HomeRoutes("home/Interesante")
    object Retos : HomeRoutes("home/retos")
}