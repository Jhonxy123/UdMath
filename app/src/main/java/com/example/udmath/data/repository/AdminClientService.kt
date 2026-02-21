package com.example.udmath.data.repository

import com.example.udmath.domain.repository.AdminRepository
import com.example.udmath.presentation.admin.AdminUserRow
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AdminClientService @Inject constructor(
    private val firestore: FirebaseFirestore
) : AdminRepository {

    override suspend fun fetchUsers(): List<AdminUserRow> {
        val snap = firestore.collection("usuarios").get().await()
        return snap.documents.map { doc ->
            AdminUserRow(
                id = doc.id,
                name = doc.getString("name").orEmpty(),
                email = doc.getString("email").orEmpty()
            )
        }
    }

    override suspend fun deleteUser(userId: String) {
        firestore.collection("usuarios").document(userId).delete().await()
    }

    override suspend fun getUser(userId: String): AdminUserRow {
        val doc = firestore.collection("usuarios").document(userId).get().await()
        return AdminUserRow(
            id = doc.id,
            name = doc.getString("name").orEmpty(),
            email = doc.getString("email").orEmpty()
        )
    }

    override suspend fun updateUser(userId: String, name: String, email: String) {
        firestore.collection("usuarios").document(userId)
            .update(mapOf("name" to name, "email" to email))
            .await()
    }
}