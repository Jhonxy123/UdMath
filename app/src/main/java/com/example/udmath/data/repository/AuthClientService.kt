package com.example.udmath.data.repository

import android.app.Activity
import com.example.udmath.domain.model.User
import com.example.udmath.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume


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



    override suspend fun loginWithMicrosoft(activity: Activity): Result<User> =
        suspendCancellableCoroutine { cont ->

            val provider = OAuthProvider.newBuilder("microsoft.com")
            // Si quisieras scopes extra:
            // provider.scopes = arrayListOf("openid", "email", "profile")

            val pending = firebaseAuth.pendingAuthResult
            if (pending != null) {
                // Ya había un flujo en progreso
                pending
                    .addOnSuccessListener { authResult ->
                        val firebaseUser = authResult.user
                        if (firebaseUser != null) {
                            cont.resume(Result.success(firebaseUser.toDomainUser()))
                        } else {
                            cont.resume(Result.failure(Exception("Usuario de Microsoft nulo")))
                        }
                    }
                    .addOnFailureListener { e ->
                        cont.resume(Result.failure(e))
                    }
            } else {
                // Iniciamos el flujo de login
                firebaseAuth
                    .startActivityForSignInWithProvider(activity, provider.build())
                    .addOnSuccessListener { authResult ->
                        val firebaseUser = authResult.user
                        if (firebaseUser != null) {
                            cont.resume(Result.success(firebaseUser.toDomainUser()))
                        } else {
                            cont.resume(Result.failure(Exception("Usuario de Microsoft nulo")))
                        }
                    }
                    .addOnFailureListener { e ->
                        cont.resume(Result.failure(e))
                    }
            }
        }

    // Mapeo FirebaseUser -> tu modelo de dominio User
    private fun FirebaseUser.toDomainUser(): User =
        User(
            id = uid,
            name = displayName ?: "",
            email = email ?: "",
            password = "" // no aplica para Microsoft
        )



    override suspend fun logout() {
        firebaseAuth.signOut()
    }


}

