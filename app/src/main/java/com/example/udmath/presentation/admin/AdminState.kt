package com.example.udmath.presentation.admin

data class AdminState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val search: String = "",
    val users: List<AdminUserRow> = emptyList()
)

data class AdminUserRow(
    val id: String = "",
    val name: String = "",
    val email: String = ""
)
