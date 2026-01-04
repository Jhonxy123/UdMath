// presentation/profile/ProfileUiState.kt
package com.example.udmath.presentation.profile

data class PerfilViewState(
    val isLoading: Boolean = true,
    val email: String = "",
    val name: String = "",
    val code: String = "",
    val photoUrl: String? = null,
    val error: String? = null
)
