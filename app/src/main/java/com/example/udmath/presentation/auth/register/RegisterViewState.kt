package com.example.udmath.presentation.auth.register

data class RegisterViewState(
    val name: String = "",
    val code: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false
)