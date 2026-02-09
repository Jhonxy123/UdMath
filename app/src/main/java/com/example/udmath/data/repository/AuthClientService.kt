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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume


class AuthClientService @Inject constructor(

    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,

) : AuthRepository {


    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser


    private suspend fun loadUserFromFirestore(uid: String): User? {
        return try {
            val snap = firestore.collection("usuarios")
                .document(uid)
                .get()
                .await()
            snap.toObject(User::class.java)
        }catch (e: Exception){
            null
        }
    }


    private suspend fun syncCurrentUser() {
        val fbUser = firebaseAuth.currentUser
        if (fbUser == null) {
            _currentUser.value = null
            return
        }

        val dbUser = loadUserFromFirestore(fbUser.uid)

        //Fallback cond datos de firebaseAuth si no existe en firestore
        _currentUser.value = dbUser?: User(
        id = fbUser.uid,
        name = fbUser.displayName ?: "",
        email = fbUser.email ?: ""
        )
    }

    override suspend fun refreshCurrentUser() {
        val fb = firebaseAuth.currentUser ?: run {
            _currentUser.value = null
            return
        }

        val snap = firestore.collection("usuarios").document(fb.uid).get().await()
        val dbUser = snap.toObject(User::class.java)

        _currentUser.value = dbUser ?: User(
            id = fb.uid,
            name = fb.displayName.orEmpty(),
            email = fb.email.orEmpty(),
            photoUrl = fb.photoUrl?.toString()
        )
    }



    //función abstracta que implementa la interfaz AuthRepository
    override suspend fun registerUser(email: String, password: String): AuthResult? {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await() //await permite esperar a que se complete la tarea sin bloquear el hilo principal
        syncCurrentUser()
        return result
    }

    // Función para registrar un usuario en Firestore

    override suspend fun registerUserInFirestore(user: User): Boolean {

        return try {

            firestore.collection("usuarios")
                .document(user.id)   // 🔑 UID como ID del documento
                .set(user)
                .await()
            true

        } catch (e: Exception) {

            false

        }

    }

    override suspend fun loginWithEmail(email: String, password: String): AuthResult {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendPasswordReset(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
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

                // Scopes básicos
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
                email = fbUser.email ?: ""
            )

            Result.success(user)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    override suspend fun logout() {
        firebaseAuth.signOut()
        _currentUser.value = null
    }


}

