package com.example.udmath.domain.repository

import android.app.Activity
import com.example.udmath.domain.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow

//Implementar funcion abstracta que define qué operaciones auth existen
interface AuthRepository{

    //*Suspen fun* es una función asíncronica compatible con corrutinas no bloquea el hilo, sino que puede “pausar” y “reanudar” su ejecución dentro de una corrutina.

    val currentUser: StateFlow<User?>

    suspend fun registerUser(email: String, password: String): AuthResult?

    suspend fun loginWithMicrosoft(activity: Activity): Result<User>

    suspend fun registerUserInFirestore(User: User): Boolean

    suspend fun logout() // funcion para cerrar sesión

}