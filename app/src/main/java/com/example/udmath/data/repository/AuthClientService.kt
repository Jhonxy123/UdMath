package com.example.udmath.data.repository

import com.example.udmath.domain.model.User
import com.example.udmath.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthClientService @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    //función abstracta que implementa la interfaz AuthRepository
    override suspend fun registerUser(email: String, password: String): AuthResult? {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).await() //await permite esperar a que se complete la tarea sin bloquear el hilo principal
    }

    override suspend fun registerUserInFirestore(User: User): Boolean {
       return try{
           firestore.collection("estudiantes").add(User).await()
           true
       }catch (e:Exception){
           false
       }
    }

}