package com.example.udmath.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object Welcome

@Serializable
object Login

@Serializable
object Register

@Serializable
object Menu

@Serializable
object Home

@Serializable
object Perfil


// para el navigation bar

@Serializable
object Main

@Serializable
object HomeTab

@Serializable
object MaterialTab

@Serializable
object PerfilTab


@Serializable
object MaterialIntTab

@Serializable
object RecomendacionesTab

@Serializable
object RetosTab

@Serializable
object SudokuTab

@Serializable
object Game2048Tab

@Serializable
object AritmeticaTab

@Serializable
object AlgebraTab

@Serializable
object FuncionesTab

@Serializable
object PaginasApoyoTab

@Serializable
object LibrosTab

@Serializable
object AplicacionesTab

@Serializable
object VideosTab

@Serializable
object ArticulosTab

@Serializable
object DatosCuriososTab

@Serializable
object MaterialAudiovisualTab

@Serializable
object ProgramacionTab


@Serializable
data class PreguntasRoute(
    val materiaId: String,
    val nivelId: String
)





