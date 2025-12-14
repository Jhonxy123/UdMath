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

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

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



    override suspend fun loginWithMicrosoft(activity: Activity): Result<User> {
        return try {
            // 1) Si hay un login pendiente (por ejemplo si el Activity se recreó)
            val pending = firebaseAuth.pendingAuthResult
            val authResult = if (pending != null) {
                pending.await()
            } else {
                // 2) Crear provider de Microsoft
                val providerBuilder = OAuthProvider.newBuilder("microsoft.com")

                // Scopes básicos (recomendados)
                providerBuilder.scopes = listOf("openid", "email", "profile")

                // Si luego quieres leer datos con Microsoft Graph, añade por ejemplo:
                // providerBuilder.scopes = listOf("openid", "email", "profile", "User.Read")

                firebaseAuth
                    .startActivityForSignInWithProvider(activity, providerBuilder.build())
                    .await()
            }

            val fbUser = authResult.user
                ?: return Result.failure(IllegalStateException("No se obtuvo usuario de Firebase"))

            // 3) Mapear a tu modelo de dominio
            val user = User(
                id = fbUser.uid,
                name = fbUser.displayName ?: "",
                email = fbUser.email ?: "",
                password = "" // no guardes password aquí (Microsoft no te la da)
            )

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    override suspend fun logout() {
        firebaseAuth.signOut()
    }


}

