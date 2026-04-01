package com.example.udmath.presentation.auth.verify_email


data class VerifyEmailViewState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val isVerified: Boolean = false
)