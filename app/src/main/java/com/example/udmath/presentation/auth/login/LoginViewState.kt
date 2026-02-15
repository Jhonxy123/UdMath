package com.example.udmath.presentation.auth.login

data class LoginViewState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,

    val loginDestination: LoginDestination? = null,

    // 🔁 Reset password
    val showResetDialog: Boolean = false,
    val resetEmail: String = "",
    val resetLoading: Boolean = false,
    val resetSuccessMessage: String? = null
)
