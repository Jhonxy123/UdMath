package com.example.udmath.domain.repository

import com.example.udmath.domain.model.User
import com.google.firebase.auth.AuthResult

//Implementar funcion abstracta
interface AuthRepository{
    suspend fun registerUser(email: String, password: String): AuthResult?
}