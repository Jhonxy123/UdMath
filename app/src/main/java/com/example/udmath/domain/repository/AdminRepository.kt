package com.example.udmath.domain.repository

import com.example.udmath.presentation.admin.AdminUserRow

interface AdminRepository {
    suspend fun fetchUsers(): List<AdminUserRow>
    suspend fun deleteUser(userId: String)
    suspend fun getUser(userId: String): AdminUserRow
    suspend fun updateUser(userId: String, name: String, email: String)
}