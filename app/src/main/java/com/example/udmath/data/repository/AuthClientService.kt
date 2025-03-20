package com.example.udmath.data.repository

import com.example.udmath.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthClientService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {


    //función abstracta que implementa la interfaz AuthRepository
    override suspend fun registerUser(email: String, password: String): AuthResult? {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).await() //await permite esperar a que se complete la tarea sin bloquear el hilo principal
    }

}