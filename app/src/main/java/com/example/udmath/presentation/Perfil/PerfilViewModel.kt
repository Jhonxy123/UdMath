// presentation/profile/ProfileViewModel.kt
package com.example.udmath.presentation.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {

            val firebaseUser = auth.currentUser

            if (firebaseUser == null) {
                _uiState.value = ProfileUiState(
                    isLoading = false,
                    error = "No hay usuario autenticado."
                )
                return@launch
            }

            _uiState.value = ProfileUiState(
                isLoading = true,
                email = firebaseUser.email.orEmpty(),
                photoUrl = firebaseUser.photoUrl?.toString()
            )

            try {

                val uid = firebaseUser.uid

                // 👇 Ajusta el nombre de colección según tu proyecto
                val doc = firestore.collection("estudiantes")
                    .document(uid)
                    .get()
                    .await()

                if (!doc.exists()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No existe perfil en Firestore para este usuario."
                    )
                    return@launch
                }

                val name = doc.getString("name").orEmpty()
                val code = doc.get("code")?.toString().orEmpty()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    name = name,
                    code = code,
                    error = null
                )
            } catch (e: Exception) {
                val se = e as? StorageException
                Log.e("Perfil", "Upload error", e)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = buildString {
                        append(e.message ?: "Error al subir imagen")
                        if (se != null) append(" (code=${se.errorCode})")
                    }
                )
            }
        }
    }

    fun uploadProfileImage(imageUri: Uri) {
        viewModelScope.launch {
            val user = auth.currentUser ?: return@launch

            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val uid = user.uid
                val storageRef = storage.reference.child("profile_images/$uid.jpg")

                // 1️⃣ Subir imagen a Storage
                storageRef.putFile(imageUri).await()

                // 2️⃣ Obtener URL pública
                val downloadUrl = storageRef.downloadUrl.await().toString()

                // 3️⃣ 🔴 AQUÍ va el set(..., merge = true)
                firestore.collection("estudiantes")
                    .document(uid)
                    .set(
                        mapOf("photoUrl" to downloadUrl),
                        SetOptions.merge()
                    )
                    .await()

                // 4️⃣ Actualizar UI
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    photoUrl = downloadUrl
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al subir imagen"
                )
            }
        }
    }


}
